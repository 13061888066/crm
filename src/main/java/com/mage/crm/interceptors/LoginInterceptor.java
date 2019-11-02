package com.mage.crm.interceptors;

import com.mage.crm.base.CrmConstant;
import com.mage.crm.service.UserService;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = CookieUtil.getCookieValue(request, "id");
        AssertUtil.isTrue(StringUtils.isBlank(id),CrmConstant.Not_LOGIN__CODE,CrmConstant.Not_LOGIN__MSG);
        User user = userService.getUserById(Integer.parseInt(Base64Util.decode(id)));//需要对id解密进行查询
        AssertUtil.isTrue(user==null,CrmConstant.Not_LOGIN__CODE,CrmConstant.Not_LOGIN__MSG);
        AssertUtil.isTrue("0".equals(user.getIsValid()),CrmConstant.Not_LOGIN__CODE,CrmConstant.Not_LOGIN__MSG);
        return true;
    }
}
