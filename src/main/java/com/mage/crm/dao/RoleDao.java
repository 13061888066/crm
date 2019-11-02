package com.mage.crm.dao;

import com.mage.crm.query.RoleQuery;
import com.mage.crm.vo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleDao {

    List<Role> queryAllRoles();

    List<Role> queryRolesByParams(RoleQuery roleQuery);

    int insert(Role role);

    Role queryRoleByRoleName(String roleName);

    int update(Role role);

    Role queryRoleById(Integer id);

    int delete(Integer id);
}
