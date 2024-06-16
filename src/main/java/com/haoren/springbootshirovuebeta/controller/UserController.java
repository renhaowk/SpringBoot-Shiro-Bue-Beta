package com.haoren.springbootshirovuebeta.controller;

import com.alibaba.fastjson.JSONObject;
import com.haoren.springbootshirovuebeta.config.annotation.RequiresPermissions;
import com.haoren.springbootshirovuebeta.service.UserService;
import com.haoren.springbootshirovuebeta.util.CommonUtil;
import com.haoren.springbootshirovuebeta.util.constants.Logical;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @RequiresPermissions("user:list")
    @GetMapping("list")
    public JSONObject listUser(HttpServletRequest request){
        return userService.listUser(CommonUtil.request2Json(request));
    }

    @RequiresPermissions("user:add")
    @PostMapping("addUser")
    public JSONObject addUser(@RequestBody JSONObject requestBody){
        CommonUtil.hasAllRequiredContent(requestBody, "username, password, nickname, roleIds");
        return userService.addUser(requestBody);
    }

    @RequiresPermissions("user:update")
    @PostMapping("updateUser")
    public JSONObject updateUser(@RequestBody JSONObject requestBody){
        CommonUtil.hasAllRequiredContent(requestBody, "nickname, roleIds, deleteStatus, userId");
        return userService.updateUser(requestBody);
    }

    /**
     * 查询系统所有role
     * return: 包含roleId,roleName的Json
     */
    @GetMapping("getAllRoles")
    @RequiresPermissions(value={"user:add","user:update"}, logical = Logical.OR)
    public JSONObject getAllRoles(){
        return userService.getAllRoles();
    }

    /**
     * 查询所有role所对应的role,user,permission信息
     * return: 包含roleId,roleName的Json
     */
    @RequiresPermissions("role:list")
    @GetMapping("/listRole")
    public JSONObject listRole() {
        return userService.listRole();
    }

    /**
     * 查询所有权限, 给角色分配权限时调用
     */
    @RequiresPermissions("role:list")
    @GetMapping("/listAllPermission")
    public JSONObject listAllPermission() {
        return userService.listAllPermission();
    }

    /**
     * 新增角色
     */
    @RequiresPermissions("role:add")
    @PostMapping("/addRole")
    public JSONObject addRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequiredContent(requestJson, "roleName,permissions");
        return userService.addRole(requestJson);
    }

    /**
     * 修改角色
     */
    @RequiresPermissions("role:update")
    @PostMapping("/updateRole")
    public JSONObject updateRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequiredContent(requestJson, "roleId,roleName,permissions");
        return userService.updateRole(requestJson);
    }

    /**
     * 删除角色
     */
    @RequiresPermissions("role:delete")
    @PostMapping("/deleteRole")
    public JSONObject deleteRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequiredContent(requestJson, "roleId");
        return userService.deleteRole(requestJson);
    }
}
