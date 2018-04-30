package com.service;

import com.common.ServerResponse;

public interface UserPostService {
    ServerResponse postSql(Long proId,Integer id,String sql,boolean accept);
    ServerResponse getPosts(Long proId,Integer uid);
    ServerResponse getPosts(Integer uid);
}
