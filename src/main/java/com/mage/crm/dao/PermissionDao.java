package com.mage.crm.dao;

import com.mage.crm.vo.Permission;

import java.util.List;

public interface PermissionDao {

    List<Integer> queryPermissionModuleIdsByRid(Integer rid);

    int queryPermissionCountByRid(Integer rid);

    int deletePermissionByRid(Integer rid);

    int insertBatch(List<Permission> permissions);

    List<String> queryPermissionsByUserId(Integer userId);
}
