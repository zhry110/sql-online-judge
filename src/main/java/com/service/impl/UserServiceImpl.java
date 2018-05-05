package com.service.impl;

import com.common.ServerResponse;
import com.dao.AdminMapper;
import com.dao.UserAcceptMapper;
import com.dao.UserMapper;
import com.pojo.Admin;
import com.pojo.UidAndAccept;
import com.pojo.User;
import com.service.UserService;
import com.vo.RankVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAcceptMapper userAcceptMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        User user = userMapper.selectByName(username);
        if (user == null)
            return ServerResponse.createByErrorMessage("用户不存在");
        if (!user.getPasswd().equals(password))
        {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPasswd("");
        Admin admin = adminMapper.selectByPrimaryKey(user.getId());
        if (admin != null)
        {
            user.setAdmin(true);
        }
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> register(String name, String passwd, String realname) {
        User user = userMapper.selectByName(name);
        if (user != null)
            return ServerResponse.createByErrorMessage("用户名已被注册");
        user = new User();
        user.setUsername(name);
        user.setPasswd(passwd);
        user.setName(realname);
        try
        {
            userMapper.insert(user);
        } catch (Exception e)
        {
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
        user = userMapper.selectByName(name);
        if (user != null)
            return ServerResponse.createBySuccessMessage("注册成功");
        return ServerResponse.createByErrorMessage("系统错误");
    }

    @Override
    public ServerResponse rankTop50(Integer uid) {
        List<UidAndAccept> rankList = userMapper.rank();
        List<RankVo> userList = null;
        Integer selfRank = userMapper.selfRank(uid);
        User self = userMapper.selectByPrimaryKey(uid);
        RankVo selfRankVo = new RankVo();
        if (rankList != null) {
            userList = new ArrayList<>(rankList.size());
            if (self != null && selfRank != null) {
                selfRankVo.setName(self.getName());
                selfRankVo.setPos(selfRank + 1);
                selfRankVo.setAccept(userAcceptMapper.userAcceptCount(uid));
                userList.add(selfRankVo);
            }

            for (UidAndAccept uidAndAccept : rankList) {
                RankVo rankVo = new RankVo();
                rankVo.setName(userMapper.selectByPrimaryKey(uidAndAccept.getUid()).getName());
                rankVo.setAccept(uidAndAccept.getAccept());
                userList.add(rankVo);
            }
        }
        return ServerResponse.createBySuccess(userList);
    }

    @Override
    public ServerResponse changePasswd(String old, String now,User user) {
        User real = userMapper.selectByPrimaryKey(user.getId());
        if (real == null)
            return ServerResponse.createByErrorMessage("用户不存在");
        if (!real.getPasswd().equals(old)) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        real.setPasswd(now);
        if (userMapper.updateByPrimaryKey(real) > 0)
            return ServerResponse.createBySuccess();
        return ServerResponse.createByErrorMessage("修改失败 无法更新密码");
    }


}
