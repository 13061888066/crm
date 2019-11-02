package com.mage.crm.controller;

import com.mage.crm.RequestPermission;
import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.service.SaleChanceService;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @RequestMapping("index/{id}")
    public String index(@PathVariable("id") String id){
        if("1".equals(id)){
            return "sale_chance";
        }else if("2".equals(id)){
            return  "cus_dev_plan";
        }else{
            return "error";
        }
    }

    @RequestMapping("querySaleChancesByParams")
    @ResponseBody
    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        Map<String, Object> map = saleChanceService.querySaleChancesByParams(saleChanceQuery);
        //前台接受的参数需要满足easyui需要的json格式
        return map;
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(SaleChance saleChance){
        saleChanceService.insert(saleChance);
        return  createDefaultMessageModel("营销机会添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(SaleChance saleChance){
        saleChanceService.update(saleChance);
        return  createDefaultMessageModel("营销机会修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    @RequestPermission(aclVal = "101003")
    public MessageModel delete(Integer[] id){
        saleChanceService.delete(id);
        return  createDefaultMessageModel("营销机会删除成功");
    }

    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public MessageModel updateSaleChanceDevResult(Integer saleChanceId,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(saleChanceId,devResult);
        return  createDefaultMessageModel("营销机会状态修改成功");
    }
}
