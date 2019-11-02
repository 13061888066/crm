package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.service.CustomerDevPlanService;
import com.mage.crm.service.SaleChanceService;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CustomerDevPlanController extends BaseController {

    @Resource
    private CustomerDevPlanService customerDevPlanService;
    @Resource
    private SaleChanceService saleChanceService;


    @RequestMapping("index")
    public String index(Integer id, Model model){
        SaleChance saleChance = saleChanceService.querySaleChancesById(id);
        System.out.println(saleChance);
        model.addAttribute("saleChance",saleChance);
        return "cus_dev_plan_detail";
    }

    @RequestMapping("queryCusDevPlans")
    @ResponseBody
    public Map<String,Object> queryCusDevPlansBySaleChanceId(CustomerDevPlanQuery customerDevPlanQuery){
        Map<String,Object> map = customerDevPlanService.queryCusDevPlansBySaleChanceId(customerDevPlanQuery);
        return map;
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(CustomerDevPlan customerDevPlan){
        MessageModel defaultMessageModel = createDefaultMessageModel("客户开发计划添加成功");
        customerDevPlanService.insert(customerDevPlan);
        return defaultMessageModel;
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(CustomerDevPlan customerDevPlan){
        MessageModel defaultMessageModel = createDefaultMessageModel("客户开发计划修改成功");
        customerDevPlanService.update(customerDevPlan);
        return defaultMessageModel;
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(CustomerDevPlan customerDevPlan){
        MessageModel defaultMessageModel = createDefaultMessageModel("客户开发计划删除成功");
        customerDevPlanService.delete(customerDevPlan.getId());
        return defaultMessageModel;
    }
}
