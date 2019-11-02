package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.base.exceptions.ParamsException;
import com.mage.crm.dto.UserDto;
import com.mage.crm.model.MessageModel;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.query.UserQuery;
import com.mage.crm.service.UserService;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public MessageModel login(String userName,String userPwd){

        MessageModel messageModel = new MessageModel();
        UserModel userModel = new UserModel();
//       try {
           userModel = userService.login(userName,userPwd);
           messageModel.setObj(userModel);
//       } catch (ParamsException pe){
//           pe.printStackTrace();
//           messageModel.setCode(pe.getCode());
//           messageModel.setMsg(pe.getMsg());
//       } catch (Exception e){
//           e.printStackTrace();
//           messageModel.setCode(CrmConstant.LOGIN_FAILED_CODE);
//           messageModel.setMsg(CrmConstant.LOGIN_FAILED_MSG);
//       }
        return messageModel;
    }

    @RequestMapping("updatePwd")
    @ResponseBody
    public  MessageModel updatePwd(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
        MessageModel messageModel = new MessageModel();
        String id = CookieUtil.getCookieValue(request, "id");
        try{
            userService.updatePwd(id, oldPassword, newPassword, confirmPassword);
            messageModel.setMsg("用户密码修改成功");
        }catch (ParamsException px){
            px.printStackTrace();
            messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
            messageModel.setMsg(px.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
            messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
        }
        return messageModel;
    }

    @RequestMapping("queryAllCustomerManager")
    @ResponseBody
    public List<User> queryAllCustomerManager(){
        System.out.println(userService.queryAllCustomerManager());
        return userService.queryAllCustomerManager();
    }

    @RequestMapping("index")
    public String index(){
        return "user";
    }


    @RequestMapping("queryUsersByParams")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
        Map<String, Object> map = userService.queryUsersByParams(userQuery);
        return map;
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(User user){
        userService.insert(user);
        return  createDefaultMessageModel("用户添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(UserDto userDto){
        userService.update(userDto);
        return  createDefaultMessageModel("用户修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        userService.delete(id);
        return  createDefaultMessageModel("用户删除成功");
    }
}
