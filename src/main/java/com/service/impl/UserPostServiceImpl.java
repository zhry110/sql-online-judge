package com.service.impl;

import com.common.ServerResponse;
import com.dao.UserAcceptMapper;
import com.pojo.UserAccept;
import com.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("UserPostService")
public class UserPostServiceImpl implements UserPostService {
    @Autowired
    private UserAcceptMapper userAcceptMapper;
    @Override
    public ServerResponse postSql(Long proId, Integer id, String sql, boolean accept) {
        if (sql.equals(""))
            return ServerResponse.createByError();
        UserAccept userAccept = new UserAccept();
        userAccept.setCorrect(accept);
        userAccept.setProId(proId);
        userAccept.setPostSql(sql);
        userAccept.setTime(new Date(System.currentTimeMillis()));
        userAccept.setUid(id);
        try {
            if (userAcceptMapper.insert(userAccept) > 0)
                return ServerResponse.createBySuccess();
            return ServerResponse.createByError();
        } catch (Exception e)
        {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse getPosts(Long proId, Integer uid) {
        return ServerResponse.createBySuccess(userAcceptMapper.problemAndUserPost(proId,uid));
    }

    @Override
    public ServerResponse getPosts(Integer uid) {
        return ServerResponse.createBySuccess(userAcceptMapper.userPost(uid));
    }
}
