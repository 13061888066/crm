package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDevPlanDao;
import com.mage.crm.dao.SaleChanceDao;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerDevPlanService {

    @Resource
    private CustomerDevPlanDao customerDevPlanDao;
    @Resource
    private SaleChanceDao saleChanceDao;

    public Map<String, Object> queryCusDevPlansBySaleChanceId(CustomerDevPlanQuery customerDevPlanQuery) {
        AssertUtil.isTrue(null == saleChanceDao.querySaleChancesById(customerDevPlanQuery.getSaleChanceId()), "营销机会不存在");
        PageHelper.startPage(customerDevPlanQuery.getPage(), customerDevPlanQuery.getRows());
        List<CustomerDevPlan> customerDevPlans = customerDevPlanDao.queryCusDevPlansBySaleChanceId(customerDevPlanQuery.getSaleChanceId());
        PageInfo<CustomerDevPlan> pageInfo = new PageInfo<>(customerDevPlans);
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        return map;
    }

    public void insert(CustomerDevPlan customerDevPlan) {
        SaleChance saleChance = saleChanceDao.querySaleChancesById(customerDevPlan.getSaleChanceId());
        AssertUtil.isTrue(saleChance == null, "营销机会不存在");
        AssertUtil.isTrue(saleChance.getDevResult() == 3, "营销机会已经完成");
        AssertUtil.isTrue(saleChance.getDevResult() == 4, "营销机会已经失败");
        customerDevPlan.setIsValid(1);
        customerDevPlan.setCreateDate(new Date());
        customerDevPlan.setUpdateDate(new Date());
        int row = customerDevPlanDao.insert(customerDevPlan);
        AssertUtil.isTrue(row < 1, "客户开发计划添加失败");
        //如果是第一次开发营销机会，并且以上的额添加计划成功，修改其开发结果
        if (saleChance.getDevResult()==0){
            saleChanceDao.updateDevResult(customerDevPlan.getSaleChanceId(),1);
        }
    }

    public void update(CustomerDevPlan customerDevPlan) {
        SaleChance saleChance = saleChanceDao.querySaleChancesById(customerDevPlan.getSaleChanceId());
        AssertUtil.isTrue(saleChance == null, "营销机会不存在");
        AssertUtil.isTrue(saleChance.getDevResult() == 3, "营销机会已经完成");
        AssertUtil.isTrue(saleChance.getDevResult() == 4, "营销机会已经失败");
        customerDevPlan.setUpdateDate(new Date());
        int row = customerDevPlanDao.update(customerDevPlan);
        AssertUtil.isTrue(row < 1, "客户开发计划更新失败");
    }

    public void delete(Integer id) {
        customerDevPlanDao.delete(id);
    }
}
