package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.service.CustomerRepriService;
import com.mage.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/customer_repri")
public class CustomerRepriController extends BaseController {

    @Resource
    private CustomerRepriService customerRepriService;

    @RequestMapping("/customerReprieveByLossId")
    @ResponseBody
    public Map<String,Object> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery){
        return customerRepriService.customerReprieveByLossId(customerRepriQuery);
    }

    @RequestMapping("/insertReprive")
    @ResponseBody
    public MessageModel insertReprive(CustomerReprieve customerReprieve){
        customerRepriService.insert(customerReprieve);
        return createDefaultMessageModel("暂缓措施添加成功");
    }
    @RequestMapping("/updateReprive")
    @ResponseBody
    public MessageModel updateReprive(CustomerReprieve customerReprieve){
        customerRepriService.update(customerReprieve);
        return createDefaultMessageModel("暂缓措施修改成功");
    }
    @RequestMapping("/delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        customerRepriService.delete(id);
        return createDefaultMessageModel("暂缓措施删除成功");
    }
}
