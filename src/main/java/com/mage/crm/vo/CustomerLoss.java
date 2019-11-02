package com.mage.crm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mage.crm.base.BaseVo;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CustomerLoss extends BaseVo {
    private Integer id;

    private String cusNo;

    private String cusName;

    private String cusManager;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date lastOrderTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date confirmLossTime;

    private Integer state;

    private String lossReason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusManager() {
        return cusManager;
    }

    public void setCusManager(String cusManager) {
        this.cusManager = cusManager;
    }

    public Date getLastOrderTime() {
        return lastOrderTime;
    }

    public void setLastOrderTime(Date lastOrderTime) {
        this.lastOrderTime = lastOrderTime;
    }

    public Date getConfirmLossTime() {
        return confirmLossTime;
    }

    public void setConfirmLossTime(Date confirmLossTime) {
        this.confirmLossTime = confirmLossTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getLossReason() {
        return lossReason;
    }

    public void setLossReason(String lossReason) {
        this.lossReason = lossReason;
    }


    @Override
    public String toString() {
        return "CustomerLoss{" +
                "id=" + id +
                ", cusNo='" + cusNo + '\'' +
                ", cusName='" + cusName + '\'' +
                ", cusManager='" + cusManager + '\'' +
                ", lastOrderTime=" + lastOrderTime +
                ", confirmLossTime=" + confirmLossTime +
                ", state=" + state +
                ", lossReason='" + lossReason + '\'' +
                '}';
    }
}
