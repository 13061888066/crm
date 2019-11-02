package com.mage.crm.model;

import com.mage.crm.base.CrmConstant;

public class MessageModel {

    private Integer code;
    private String msg;
    private Object obj;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public MessageModel(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public MessageModel() {
        this(CrmConstant.OPS_SUCCESS_CODE,CrmConstant.OPS_SUCCESS_MSG,null);
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                '}';
    }
}
