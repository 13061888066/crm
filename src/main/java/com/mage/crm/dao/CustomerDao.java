package com.mage.crm.dao;

import com.mage.crm.dto.CustomerDto;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.vo.Customer;

import java.util.List;

public interface CustomerDao {

    List<Customer> queryAllCustomers();

    List<Customer> queryCustomersByParams(CustomerQuery customerQuery);

    int insert(Customer customer);

    int update(Customer customer);

    int delete(Integer[] id);

    Customer queryCustomerById(Integer id);

    int updateCustomerState(String cusNo);

    List<CustomerDto> queryCustomersContribution(CustomerQuery customerQuery);

    List<CustomerDto> queryCustomerGC();
}
