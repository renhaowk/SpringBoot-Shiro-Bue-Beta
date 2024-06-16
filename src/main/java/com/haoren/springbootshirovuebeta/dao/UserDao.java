package com.haoren.springbootshirovuebeta.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserDao {

    /* 查询用户数量 */
    int countUser(JSONObject jsonObject);

    /* 查询用户列表*/
    List<JSONObject> listUser(JSONObject jsonObject);

    int queryExistUserName(JSONObject jsonObject);

    void addUser(JSONObject jsonObject);

    void batchAddUserRole(JSONObject jsonObject);

    void updateUser(JSONObject requestBody);

    void removeAllUserRole(int userId);

    List<JSONObject> getAllRoles();

    List<JSONObject> listRole();

    List<JSONObject> listPermission();

    void insertRole(JSONObject requestJson);

    void insertRolePermission(String roleId, List<String> rolePermissions);

    JSONObject getRoleAllInfo();

    void updateRoleName(JSONObject newRoleInfo);

    void removeOldPermissions(String roleId, Set<Integer> oldPermissions);

    void addNewPermissons(String roleId, List<Integer> newPermissions);

    int countRoleUser(String roleId);

    void removeRole(@Param("roleId") String roleId);

    void removeRoleAllPermissions(@Param("roleId") String roleId);
}
