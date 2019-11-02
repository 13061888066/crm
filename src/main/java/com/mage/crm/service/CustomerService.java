package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDao;
import com.mage.crm.dto.CustomerDto;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Customer;
import com.mage.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    @Resource
    private CustomerDao customerDao;

    public List<Customer> queryAllCustomers() {
        return customerDao.queryAllCustomers();
    }

    public Map<String, Object> queryCustomersByParams(CustomerQuery customerQuery) {
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getRows());
        List<Customer> customers = customerDao.queryCustomersByParams(customerQuery);
        PageInfo<Customer> pageInfo = new PageInfo<>(customers);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        return map;
    }

    public void insert(Customer customer) {
        checkParams(customer.getName(),customer.getFr(),customer.getPhone());
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setIsValid(1);
        customer.setState(0);
        String khno = "KH"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        customer.setKhno(khno);
        int row = customerDao.insert(customer);
        AssertUtil.isTrue(row<1,"客户添加失败");
    }

    public void update(Customer customer) {
        checkParams(customer.getName(),customer.getFr(),customer.getPhone());
        customer.setUpdateDate(new Date());
        int row = customerDao.update(customer);
        AssertUtil.isTrue(row<1,"客户修改失败");
    }

    public void delete(Integer[] id) {
        int row = customerDao.delete(id);
        AssertUtil.isTrue(row<id.length,"客户删除失败");
    }

    //做非空校验，客户名称、法人、联系电话
    public void checkParams(String customerName, String fr, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(fr), "法人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "联系电话不能为空");
    }

    public Customer queryCustomerById(Integer id) {
        return customerDao.queryCustomerById(id);
    }

    public Map<String, Object> queryCustomersContribution(CustomerQuery customerQuery) {
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getRows());
        List<CustomerDto> customerDtos = customerDao.queryCustomersContribution(customerQuery);
        PageInfo<CustomerDto> pageInfo = new PageInfo<>(customerDtos);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        return map;
    }

    public Map<String, Object> queryCustomerGC() {
        List<CustomerDto> customerDtos = customerDao.queryCustomerGC();
        Map<String, Object> map = new HashMap<>();
        map.put("code",300);
        Integer[] counts;
        String[] levels;
        if(customerDtos!=null && customerDtos.size()>1){
            map.put("code",200);
            counts = new Integer[customerDtos.size()];
            levels = new String[customerDtos.size()];
            for(int i=0;i<customerDtos.size();i++){
                counts[i] = customerDtos.get(i).getCount();
                levels[i] = customerDtos.get(i).getLevel();
            }
            map.put("counts",counts);
            map.put("levels",levels);
        }
        return map;

    }
}
