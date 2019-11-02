package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.base.exceptions.ParamsException;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.UserDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.dto.UserDto;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.Md5Util;
import com.mage.crm.vo.Permission;
import com.mage.crm.vo.SaleChance;
import com.mage.crm.vo.User;
import com.mage.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private HttpSession session;

    public User getUserById(Integer id) {
        return  userDao.getUserById(id);
    }

    public int updateByParams(User record) {
        return userDao.updateByParams(record);
    }

    public int updateUser(User user) {
        return userDao.updateUser(user);
    }


    public UserModel login(String userName,String userPwd) {

        User user = userDao.getUserByName(userName);
        //user为空
        System.out.println("user="+user);
        AssertUtil.isTrue(null==user,"用户不存在");
        //user的密码和传来的密码不匹配
        String s = Md5Util.enCode(userPwd);
        System.out.println(s.equals(user.getUserPwd()));
        AssertUtil.isTrue(!(s.equals(user.getUserPwd())),"用户名或密码不正确");
        //用户的标识码为0，无效
        System.out.println(user.getIsValid());
        AssertUtil.isTrue(0==user.getIsValid(),"用户已注销");
        //满足所有条件，创建对应的UserModel返回给controller
        //将userPermission存入session中用于权限判定
        List<String> permissions = permissionDao.queryPermissionsByUserId(user.getId());
        System.out.println(permissions);
        if(null!=permissions&&permissions.size()>0) {
            session.setAttribute(CrmConstant.USER_PERMISSION, permissions);
        }
        return createUserModel(user);
    }

    public UserModel createUserModel(User user){

        UserModel userModel = new UserModel();
        userModel.setId(Base64Util.enCode(user.getId().toString()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return  userModel;

    }

    public void updatePwd(String id,String oldPwd,String newPwd,String confirmPwd) {

        //user为空
        AssertUtil.isTrue(StringUtils.isBlank(id),"id不存在");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        AssertUtil.isTrue(!newPwd.equals(confirmPwd),"新密码与确认密码不一致");
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码与旧密码不能相等");
        //base64解码后的id
        int id2 = Integer.parseInt(Base64Util.decode(id));
        User user = userDao.getUserById(id2);
        AssertUtil.isTrue(null==user,"用户不存在了");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销");
        AssertUtil.isTrue(!Md5Util.enCode(oldPwd).equals(user.getUserPwd()),"旧密码错误");
        User user1 = new User();
        user1.setId(id2);
        user1.setUserPwd(Md5Util.enCode(newPwd));
        int row = userDao.updatePwd(user1);
        AssertUtil.isTrue(row<1,"密码更新失败");
    }

    public List<User> queryAllCustomerManager() {
        return userDao.queryAllCustomerManager();
    }

    public Map<String, Object> queryUsersByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPage(),userQuery.getRows());
        List<UserDto> userDtos = userDao.queryUsersByParams(userQuery);
        if(userDtos!=null && userDtos.size()>0){
            for(UserDto userDto:userDtos){
                String roleIdsStr = userDto.getRoleIdsStr();
                if(roleIdsStr!=null&&roleIdsStr!=""){
                    String[] strs = roleIdsStr.split(",");
                    List<Integer> roleIds = userDto.getRoleIds();
                    for(int i=0;i<strs.length;i++){
                        roleIds.add(Integer.parseInt(strs[i]));
                    }
                }
            }
        }
        PageInfo<UserDto> pageInfo = new PageInfo<>(userDtos);
        Map<String,Object> map = new HashMap<>();
        map.put("rows",pageInfo.getList());
        map.put("total",pageInfo.getTotal());
        return map;
    }

    public void insert(User user) {
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone());
        //判定用户的唯一性
        AssertUtil.isTrue(userDao.getUserByName(user.getUserName()) != null, "用户名已经存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.enCode("123456"));
        int insert = userDao.insert(user);
        AssertUtil.isTrue(insert<1,"用户添加失败");
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null && roleIds.size()>0){
            relateRoles(roleIds,user.getId());
        }
    }

    /**
     * 用户表和用户角色表级联，先修改用户表，然后对用户角色表先删除后插入完成修改操作
     * @param user
     */
    public void update(User user) {
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone());
        user.setUpdateDate(new Date());
        User u = userDao.getUserByName(user.getUserName());
        //判定用户的唯一性
        AssertUtil.isTrue(u!= null&&!u.getId().equals(user.getId()), "用户名已经存在");

        int update = userDao.updateUser(user);
        AssertUtil.isTrue(update<1,"用户修改失败");
        //根据id删除用户角色
        userRoleDao.deleteUserRolesByUserId(user.getId());
        List<Integer> roleIds = user.getRoleIds();
        //根据id添加用户角色
        if(roleIds!=null && roleIds.size()>0){
            relateRoles(roleIds,user.getId());
        }
    }

    public void checkParams(String userName,String trueName,String phone){
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(trueName),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空");
    }
    public void relateRoles(List<Integer> roleIds,Integer userId){
        ArrayList<UserRole> userRoles = new ArrayList<>();
        for(int i=0;i<roleIds.size();i++){
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleIds.get(i));
            userRole.setUserId(userId);
            userRole.setIsValid(1);
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            userRoles.add(userRole);
        }
        AssertUtil.isTrue(userRoleDao.insertBacth(userRoles)<1,"用户角色添加失败");
    }

    //根据id删除用户，级联删除用户角色
    public void delete(Integer id) {
        int delete = userDao.delete(id);
        AssertUtil.isTrue(delete<1,"用户删除失败");
        int i = userRoleDao.deleteUserRolesByUserId(id);
        AssertUtil.isTrue(i<1,"用户角色级联删除失败");
    }
}