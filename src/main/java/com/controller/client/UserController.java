package com.controller.client;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String name, String passwd, HttpSession session)
    {
        if (name == null || passwd == null)
            return ServerResponse.createByErrorMessage("null request");
        ServerResponse<User> response = userService.login(name,passwd);
        if(response.isSuccess()){
            session.setAttribute(Const.CUR_USER,response.getData());
        }
        return response;
    }
    @RequestMapping(value = "userinfo.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> userInfo(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CUR_USER);
        if (user == null)
            return ServerResponse.createByError();
        return ServerResponse.createBySuccess(user);
    }
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> logout(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CUR_USER);
        if (user == null)
            return ServerResponse.createByErrorMessage("未登录");
        session.setAttribute(Const.CUR_USER,null);
        return ServerResponse.createBySuccess();
    }
    //name:name,passwd:passwd,realname:realname
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(String name,String passwd,String realname)
    {
        if (name.contains(" "))
            return ServerResponse.createByErrorMessage("用户名不能包含空格");
        if (realname.contains(" "))
            return ServerResponse.createByErrorMessage("姓名不能包含空格");
        if (passwd.contains(" "))
            return ServerResponse.createByErrorMessage("密码不能包含空格");
        if (name.equals(""))
            return ServerResponse.createByErrorMessage("用户名不能为空");
        if (passwd.equals(""))
            return ServerResponse.createByErrorMessage("密码不能为空");
        if (realname.equals(""))
            return ServerResponse.createByErrorMessage("姓名不能为空");
        return userService.register(name,passwd,realname);
    }
    @RequestMapping(value = "rank.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse rank(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CUR_USER);
        Integer uid = null;
        if (user != null)
            uid = user.getId();
        return userService.rankTop50(uid);
    }
    @RequestMapping(value = "changePassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse changePasswd(String old,String now,HttpSession session) {
        User user = (User) session.getAttribute(Const.CUR_USER);
        if (user == null)
            return ServerResponse.createByErrorMessage("未登录");
        return userService.changePasswd(old,now,user);
    }
}
