package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.RoleDao;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import com.mage.crm.vo.Permission;
import com.mage.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModuleDao moduleDao;

    public List<Role> queryAllRoles() {
        return roleDao.queryAllRoles();
    }

    public Map<String, Object> queryRolesByParama(RoleQuery roleQuery) {
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getRows());
        List<Role> roles= roleDao.queryRolesByParams(roleQuery);
        PageInfo<Role> pageInfo=new PageInfo<>(roles);
        Map<String, Object> map=new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());
        return map;
    }

    /**
     *  参数判定，用户角色名不能为空，且不能重复
     * @param role
     */
    public void insert(Role role) {

        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"用户角色名不能为空");
        AssertUtil.isTrue(null != roleDao.queryRoleByRoleName(role.getRoleName()),"用户角色名已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        int insert = roleDao.insert(role);
        AssertUtil.isTrue(insert<1,"用户角色添加失败");
    }

    /**
     * 参数判定，用户角色名不能为空，且不能重复
     * @param role
     */
    public void update(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"用户角色名不能为空");
        AssertUtil.isTrue(null == role.getId() || null == roleDao.queryRoleById(role.getId()),"用户角色不存在");
        Role role1 = roleDao.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != role1 && !role.getId().equals(role1.getId()),"用户角色名已存在");
        role.setUpdateDate(new Date());
        int update = roleDao.update(role);
        AssertUtil.isTrue(update<1,"用户角色修改失败");
    }

    public void delete(Integer id) {
        int delete = roleDao.delete(id);
        AssertUtil.isTrue(delete<1,"用户角色删除失败");
    }

    /**
     * 1.参数合法性校验
     *    rid 角色记录必须存在
     *    moduleId  可空
     * 2.删除原始权限
     *     查询原始权限
     *         原始权限存在  执行删除操作
     * 3. 执行批量添加
     *     根据moduleId  查询每个模块  权限值
     *     List<Permission>
     */
    public void addPermission(Integer rid, Integer[] moduleIds) {
        System.out.println(moduleIds);
        //rid非空判定，以及角色是否存在的判定
        AssertUtil.isTrue(null==rid || null==roleDao.queryRoleById(rid), "待授权的角色不存在!");
        //查询指定rid对应的权限记录数
        int count = permissionDao.queryPermissionCountByRid(rid);
        //1.根据rid删除对应的permission中的记录
        if(count>0){
            int deletePermissionByRid = permissionDao.deletePermissionByRid(rid);
            AssertUtil.isTrue(deletePermissionByRid<count, CrmConstant.OPS_FAILED_MSG);
        }
        //2.根据moduleIds添加permission(需要通过批量添加将module中的opt——value操作码设置成acl_value)
        List<Permission> permissions = null;
        //如果接收到的modules为空或者长度为0
        if(moduleIds!=null && moduleIds.length>0){
            permissions = new ArrayList<>();
            Module module = null;
            for(Integer moduleId:moduleIds){
                System.out.println(moduleId);
                module = moduleDao.queryModuleById(moduleId);
                Permission permission = new Permission();
                if(module != null){
                    permission.setAclValue(module.getOptValue());
                }
                permission.setRoleId(rid);
                permission.setModuleId(moduleId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permissions.add(permission);
            }
        }
        int insertBatch = permissionDao.insertBatch(permissions);
        AssertUtil.isTrue(insertBatch<moduleIds.length,CrmConstant.OPS_FAILED_MSG);
    }
}
