package com.haoren.springbootshirovuebeta.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SessionUserInfo {
        private int userId;
        private String username;
        private String nickname;
        private List<Integer> roleIds;
        private Set<String> menuList;
        private Set<String> permissionList;
}
