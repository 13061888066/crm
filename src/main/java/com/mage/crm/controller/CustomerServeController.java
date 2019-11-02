package com.mage.crm.controller;

import com.mage.crm.service.CustomerServeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_serve")
public class CustomerServeController {

    @Resource
    private CustomerServeService customerServeService;

    @RequestMapping("queryCustomerServeType")
    @ResponseBody
    public Map<String,Object> queryCustomerServeType(){
        Map<String, Object> map = customerServeService.queryCustomerServeType();
        return map;
    }

}
