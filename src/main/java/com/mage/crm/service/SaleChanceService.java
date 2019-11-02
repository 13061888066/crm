package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.SaleChanceDao;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService {

    @Resource
    private SaleChanceDao saleChanceDao;

    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery) {
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
        List<SaleChance> saleChances = saleChanceDao.querySaleChancesByParams(saleChanceQuery);
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChances);
        Map<String,Object> map = new HashMap<>();
        map.put("rows",pageInfo.getList());
        map.put("total",pageInfo.getTotal());
        return map;
    }


    public void insert(SaleChance saleChance) {
        checkParams(saleChance.getLinkMan(),saleChance.getLinkPhone(),saleChance.getCgjl());
        //设置创建时间和更新时间
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setDevResult(0);
        saleChance.setIsValid(1);
        //没有勾选分配人时，状态为0，有则为1
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
        }else{
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
        }
        int row = saleChanceDao.insert(saleChance);
        AssertUtil.isTrue(row<1,"营销机会添加失败");
    }

    public void update(SaleChance saleChance) {
        checkParams(saleChance.getLinkMan(),saleChance.getLinkPhone(),saleChance.getCgjl());
        //设置更新时间
        saleChance.setUpdateDate(new Date());
        //没有勾选分配人时，状态为0，有则为1
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
        }else{
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
        }
        int row = saleChanceDao.update(saleChance);
        AssertUtil.isTrue(row<1,"营销机会修改失败");
    }

    public void delete(Integer[] id) {
        int row = saleChanceDao.delete(id);
        AssertUtil.isTrue(row<id.length,"营销机会删除失败");
    }

    //做非空校验，联系人、联系电话、成功几率
    public void checkParams(String linkMan,String linkPhone,Integer cgjl){
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        AssertUtil.isTrue(cgjl<0||cgjl>100||cgjl==null,"成功几率不能小于0或大于100");
    }


    public SaleChance querySaleChancesById(Integer id) {
        return  saleChanceDao.querySaleChancesById(id);
    }

    public void updateSaleChanceDevResult(Integer saleChanceId,Integer devResult) {
        saleChanceDao.updateDevResult(saleChanceId,devResult);
    }
}
