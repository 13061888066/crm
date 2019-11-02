package com.mage.crm.dao;



import com.mage.crm.dto.UserDto;
import com.mage.crm.query.UserQuery;
import com.mage.crm.vo.User;

import java.util.List;

public interface UserDao {

    /**
     * 添加用户,返回对应的主键
     * @param user
     * @return
     */
    int add(User user);

    /**
     * 根据id删除单个用户
     * @param id
     * @return
     */
    int delete(Integer id);

    /**
     * 查询所有用户信息
     * @return
     */
    List<User> getUserList();

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     */
    User getUserById(Integer id);

    /**
     *
     * @param userName
     * @return
     */
    User getUserByName(String userName);

    /**
     * 多条件修改(至少要修改一个属性)
     * @param record
     * @return
     */
     int updateByParams(User record);

    /**
     * 修改用户所有信息
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 根据id修改用户密码
     * @param user
     * @return
     */
    int updatePwd(User user);

    List<User> queryAllCustomerManager();

    List<UserDto> queryUsersByParams(UserQuery userQuery);

    int insert(User user);

}