package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.service.CustomerService;
import com.mage.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("customer")
@Controller
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    @RequestMapping("queryAllCustomers")
    @ResponseBody
    public List<Customer> queryAllCustomers(){
        return  customerService.queryAllCustomers();
    }

    @RequestMapping("index")
    public String index(){
        return  "customer";
    }

    @RequestMapping("queryCustomersByParams")
    @ResponseBody
   public Map<String,Object> queryCustomersByParams(CustomerQuery customerQuery){
        Map<String, Object> map = customerService.queryCustomersByParams(customerQuery);
        return map;
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(Customer customer){
        customerService.insert(customer);
        return createDefaultMessageModel("客户添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(Customer customer){
        customerService.update(customer);
        return createDefaultMessageModel("客户修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer[] id){
        customerService.delete(id);
        return createDefaultMessageModel("客户删除成功");
    }

    @RequestMapping("openCustomerOtherInfo/{type}/{id}")
    public String openCustomerOtherInfo(@PathVariable("type") String type, @PathVariable("id")Integer id, Model model){
        Customer customer = customerService.queryCustomerById(id);
        model.addAttribute("customer",customer);
        switch (type){
            case "1":return "customer_linkMan";
            case "2":return "customer_concat";
            case "3":return "customer_order";
            default:return "error";
        }
    }

    @RequestMapping("queryCustomersContribution")
    @ResponseBody
    public Map<String,Object> queryCustomersContribution(CustomerQuery customerQuery){
        Map<String, Object> map = customerService.queryCustomersContribution(customerQuery);
        return map;
    }

    @RequestMapping("queryCustomerGC")
    @ResponseBody
    public Map<String,Object> queryCustomerGC(){
        Map<String, Object> map = customerService.queryCustomerGC();
        return map;
    }

}
