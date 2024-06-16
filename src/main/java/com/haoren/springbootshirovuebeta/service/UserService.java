package com.haoren.springbootshirovuebeta.service;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.haoren.springbootshirovuebeta.dao.UserDao;
import com.haoren.springbootshirovuebeta.util.CommonUtil;
import com.haoren.springbootshirovuebeta.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public JSONObject listUser(JSONObject jsonObject) {
        CommonUtil.fillPagesParams(jsonObject);
        int count = userDao.countUser(jsonObject);
        List<JSONObject> list = userDao.listUser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    public JSONObject addUser(JSONObject jsonObject) {
        int count = userDao.queryExistUserName(jsonObject);
        if (count > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }
        userDao.addUser(jsonObject);
        userDao.batchAddUserRole(jsonObject);
        return CommonUtil.successJson();
    }

    public JSONObject updateUser(JSONObject requestBody) {
        //不能修改管理员
        if (requestBody.getIntValue("userId") == 10001) {
            return CommonUtil.successJson();
        }
        userDao.updateUser(requestBody);
        userDao.removeAllUserRole(requestBody.getIntValue("userId")); //先删除用户所有role，再更新新的role
        if(!requestBody.getJSONArray("roleIds").isEmpty()){
            userDao.batchAddUserRole(requestBody);
        }
        return CommonUtil.successJson();
    }

    public JSONObject getAllRoles() {
        List<JSONObject> roles = userDao.getAllRoles();
        return CommonUtil.successPage(roles);
    }

    public JSONObject listRole() {
        List<JSONObject> listRoles = userDao.listRole();
        return CommonUtil.successJson(listRoles);
    }


    public JSONObject listAllPermission() {
        List<JSONObject> listPermissions = userDao.listPermission();
        return CommonUtil.successJson(listPermissions);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addRole(JSONObject requestJson) {
        userDao.insertRole(requestJson);
        userDao.insertRolePermission(requestJson.getString("roleId"), (List<String>)requestJson.get("rolePermissions"));
        return CommonUtil.successJson();
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateRole(JSONObject requestJson) {
        String roleId = requestJson.getString("roleId");
        List<Integer> newPermissions = (List<Integer>) requestJson.get("rolePermissions");
        JSONObject roleAllInfo = userDao.getRoleAllInfo();
        Set<Integer> oldPermissions = (Set<Integer>) roleAllInfo.get("permissionIds");
        //更新role的名字
        updateRoleName(roleAllInfo, requestJson);
        //移除旧role的权限
        removeOldRolePermissions(roleId, oldPermissions);
        //添加新role的权限
        addNewRolePermissions(roleId, newPermissions);
        return CommonUtil.successJson();
    }

    private void updateRoleName(JSONObject oldRoleInfo, JSONObject newRoleInfo) {
        String newRoleName = newRoleInfo.getString("roleName");
        if(!newRoleName.equals(oldRoleInfo.getString("roleName"))){
            userDao.updateRoleName(newRoleInfo);
        }
    }

    private void removeOldRolePermissions(String roleId, Set<Integer> oldPermissions) {
        if(oldPermissions.size()>0){
            userDao.removeOldPermissions(roleId, oldPermissions);
        }
    }

    private void addNewRolePermissions(String roleId, List<Integer> newPermissions) {
        userDao.addNewPermissons(roleId, newPermissions);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject deleteRole(JSONObject requestJson) {
        String roleId = requestJson.getString("roleId");
        int roleInUseCount = userDao.countRoleUser(roleId);
        if(roleInUseCount > 0){
            return CommonUtil.errorJson(ErrorEnum.E_10008);
        }
        userDao.removeRole(roleId);
        userDao.removeRoleAllPermissions(roleId);
        return CommonUtil.successJson();
    }
}
