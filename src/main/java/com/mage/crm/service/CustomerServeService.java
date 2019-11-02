package com.mage.crm.service;

import com.mage.crm.dao.CustomerServeDao;
import com.mage.crm.dto.CustomerDto;
import com.mage.crm.dto.CustomerServeDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServeService {

    @Resource
    private CustomerServeDao customerServeDao;

    public Map<String, Object> queryCustomerServeType() {
        List<CustomerServeDto> customerServeDtoList = customerServeDao.queryCustomerServeType();
        Map<String, Object> map = new HashMap<>();
        map.put("code",300);
        String[] name;
        CustomerServeDto[]  customerServeDtos;
        if(customerServeDtoList!=null && customerServeDtoList.size()>1){
            map.put("code",200);
            name = new String[customerServeDtoList.size()];
            customerServeDtos = new CustomerServeDto[customerServeDtoList.size()];
            for(int i=0;i<customerServeDtoList.size();i++){
                name[i] = customerServeDtoList.get(i).getName();
                customerServeDtos[i] = customerServeDtoList.get(i);
            }
            map.put("name",name);
            map.put("customerServeDtos",customerServeDtos);
        }
        return map;
    }
}
