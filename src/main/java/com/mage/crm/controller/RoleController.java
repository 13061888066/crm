package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.service.RoleService;
import com.mage.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    /**
     * 查询所有有效的用户角色
     * @return
     */
    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public List<Role> queryAllRoles(){
        return roleService.queryAllRoles();
    }

    @RequestMapping("index")
    public String index(){
        return "role";
    }


    @RequestMapping("queryRolesByParams")
    @ResponseBody
    public Map<String,Object> queryRolesByParams(RoleQuery roleQuery){
        return roleService.queryRolesByParama(roleQuery);
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(Role role){
        roleService.insert(role);
        return  createDefaultMessageModel("用户角色添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(Role role){
        roleService.update(role);
        return  createDefaultMessageModel("用户角色修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        roleService.delete(id);
        return  createDefaultMessageModel("用户角色删除成功");
    }

    @RequestMapping("addPermission")
    @ResponseBody
    public MessageModel addPermission(Integer rid,Integer[] moduleIds){
        roleService.addPermission(rid,moduleIds);
        return  createDefaultMessageModel("用户角色授权成功");
    }
}
