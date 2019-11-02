package com.mage.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModuleService {

    @Resource
    private ModuleDao moduleDao;
    @Resource
    private PermissionDao permissionDao;

    public List<ModuleDto> queryAllsModuleDtos(Integer rid) {
        //将查出来的moduelDtos设置checked属性
        List<ModuleDto> moduleDtos = moduleDao.queryAllsModuleDtos();
        //根据角色id查询对应permission信息，
        List<Integer> moduleIds = permissionDao.queryPermissionModuleIdsByRid(rid);
        //当moduleDtos中包含permissions中的moduleId，则设置checked属性为true；
        for (ModuleDto moduleDto : moduleDtos) {
            //1.通过for循环遍历
//            for(int i=0;i<moduleIds.size();i++){
//                if(moduleDto.getId().equals(moduleIds.get(i))){
//                    moduleDto.setChecked(true);
//                }
//            }
//        }
            //2通过map
            if (moduleIds.contains(moduleDto.getId())) {
                moduleDto.setChecked(true);
            }
        }
        return moduleDtos;
    }


    public Map<String, Object> queryModulesByParams(ModuleQuery moduleQuery) {
        PageHelper.startPage(moduleQuery.getPage(), moduleQuery.getRows());
        List<Module> modules = moduleDao.queryModulesByParams(moduleQuery);
        PageInfo<Module> pageInfo = new PageInfo<>(modules);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        return map;
    }

    public List<Module> queryModulesByGrade(Integer grade) {
        return moduleDao.queryModulesByGrade(grade);
    }

    /**
     * 1.参数校验
     * 模块名称非空
     * 层级 非空
     * 模块权限值 非空
     * 2.权限值不能重复
     * 3.每一层  模块名称不能重复
     * 4.非根级菜单 上级菜单必须存在
     * 5.设置额外字段
     * isValid
     * createDate
     * updateDate
     * 6.执行添加
     */
    public void insert(Module module) {
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue());
        AssertUtil.isTrue(null != moduleDao.queryModuleByOptValue(module.getOptValue()), "权限值不能重复!");
        AssertUtil.isTrue(null != moduleDao.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName()), "同一层模块名称不能重复!");
        if (module.getGrade() != 0) {
            AssertUtil.isTrue(null == moduleDao.queryModuleByParentId(module.getParentId()), "父级菜单不存在!");
        }
        module.setIsValid(1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        int insert = moduleDao.insert(module);
        AssertUtil.isTrue(insert < 1, "模块添加失败!");
    }

    public void update(Module module) {
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue(), module.getId());
        Module tmp = moduleDao.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null != tmp && !tmp.getId().equals(module.getId()), "权限值不能重复!");
        tmp = moduleDao.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName());
        AssertUtil.isTrue(null != tmp && !tmp.getId().equals(module.getId()), "同一层模块名称不能重复!");
        if (module.getGrade() != 0) {
            AssertUtil.isTrue(null == moduleDao.queryModuleByParentId(module.getParentId()), "父级菜单不存在!");
        }
        module.setUpdateDate(new Date());
        int update = moduleDao.update(module);
        AssertUtil.isTrue(update < 1, "模块修改失败!");
    }

    /**
     * 递归删除
     *
     * @param id
     */
    public void delete(Integer id) {
        AssertUtil.isTrue(null == id || null == moduleDao.queryModuleById(id), "待删除模块不存在!");
        List<Integer> ids = new ArrayList<>();
        getIds(id, ids);
        int delete = moduleDao.delete(ids);
        AssertUtil.isTrue(delete<ids.size(), "模块删除失败!");
    }

    public List<Integer> getIds(Integer id, List<Integer> ids) {
        //根据id获取根节点
        Module module = moduleDao.queryModuleById(id);
        if (null != module) {
            ids.add(module.getId());
            //获取根的下一级节点
            List<Module> modules = moduleDao.querySubModulesByParentId(module.getId());
            if (null != modules && modules.size() > 0) {
                for (Module m : modules) {
                    //如果还有孩子节点，就继续迭代
                    getIds(m.getId(), ids);
                }
            }
        }
        return ids;
    }

    /**
     * 参数校验
     * 模块名称非空
     * 层级 非空，且必须在0，1，2中
     * 模块权限值 非空
     */
    private void checkModuleParams(String moduleName, Integer grade, String optValue) {
        AssertUtil.isTrue(StringUtils.isBlank(moduleName), "模块名不能为空!");
        AssertUtil.isTrue(null == grade, "层级值不能为空!");
        Boolean flag = (grade != 0 && grade != 1 && grade != 2);
        AssertUtil.isTrue(flag, "层级值非法!");
        AssertUtil.isTrue(StringUtils.isBlank(optValue), "权限值不能为空!");
    }

    private void checkModuleParams(String moduleName, Integer grade, String optValue, Integer id) {
        checkModuleParams(moduleName, grade, optValue);
        AssertUtil.isTrue(null == id || null == moduleDao.queryModuleById(id), "待更新模块不存在!");
    }
}
