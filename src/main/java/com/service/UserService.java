package com.service;

import com.common.ServerResponse;
import com.pojo.User;

public interface UserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(String name, String passwd, String realname);

    ServerResponse rankTop50(Integer uid);

}
