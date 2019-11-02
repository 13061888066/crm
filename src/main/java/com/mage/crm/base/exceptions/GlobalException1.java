package com.mage.crm.base.exceptions;

import com.alibaba.fastjson.JSON;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.model.MessageModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class GlobalException1 implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //判定handler是否属于HandlerMethod，json异常时用于强转
        ModelAndView modelAndView = createDefaultModelAndView(request);
        ParamsException pe;
        if (handler instanceof HandlerMethod) {
            //1.json异常
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ResponseBody annotation = method.getAnnotation(ResponseBody.class);
            if (null != annotation) {
                System.out.println("json异常开始");
                //默认传回的messageModel
                MessageModel messageModel = new MessageModel();
                messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
                messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
                if (ex instanceof ParamsException) {
                    System.out.println("进入了自定义异常");
                    pe = (ParamsException) ex;
                    messageModel.setCode(pe.getCode());
                    messageModel.setMsg(pe.getMsg());
                }
                response.setContentType("application/json;charset=uft-8");
                response.setCharacterEncoding("utf-8");
                //创建字节流，将数据以json形式返回给前台
                PrintWriter writer = null;
                try {
                    writer = response.getWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(writer!=null){
                        writer.write(JSON.toJSONString(messageModel));
                        writer.flush();
                        writer.close();
                    }
                }
                return null;
            }
            //2.视图异常（包括未登录异常）
            if (ex instanceof ParamsException) {
                pe = (ParamsException) ex;
                modelAndView.addObject("code", pe.getCode());
                modelAndView.addObject("msg", pe.getMsg());
                return modelAndView;
            }
        }
        return modelAndView;
    }

    public static ModelAndView createDefaultModelAndView(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("code", CrmConstant.OPS_FAILED_CODE);
        modelAndView.addObject("msg", CrmConstant.OPS_FAILED_MSG);
        modelAndView.addObject("uri", request.getRequestURI());//获取当前资源名
        modelAndView.addObject("ctx", request.getContextPath());//被拦截时，设置ctx
        return modelAndView;
    }
}
