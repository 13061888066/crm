package com.mage.crm.dao;

import com.mage.crm.query.CustomerLossQuery;
import com.mage.crm.vo.CustomerLoss;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerLossDao {

    List<CustomerLoss> queryCustomerLossesByParams(CustomerLossQuery customerLossQuery);

//   CustomerLoss queryCustomerLossesById(Integer lossId);

    Map<String,Object> queryCustomerLossesById(Integer id);

    int updateCustomerLossState(@Param("id") Integer id, @Param("reason") String reason);
}
