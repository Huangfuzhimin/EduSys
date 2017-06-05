package org.itheima.edu;

import org.itheima.edu.dao.TokenEntityDao;
import org.itheima.edu.dao.UserEntityDao;
import org.itheima.edu.entity.TokenEntity;
import org.itheima.edu.entity.UserEntity;
import org.itheima.edu.util.LogUtils;
import org.itheima.edu.util.ResponseCode;
import org.itheima.edu.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Poplar on 2016/12/28.
 */
@Controller
public class UserController {
    @Autowired
    UserEntityDao userDao;

    @Autowired
    TokenEntityDao tokenDao;

    // 一天
    private int ONE_DAY = 24 * 60 * 60 * 1000;

    @RequestMapping("/register")
    @ResponseBody
    public String register(HttpServletRequest request, String account, String password) {
        return ResponseUtils.success(ResponseCode.Success.DEFAULT, "账号注册成功");
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest request, String account, String password) {
        LogUtils.printRequest(request);

        if (StringUtils.isEmpty(account)) {
            return ResponseUtils.error(ResponseCode.LoginError.LOST_USER, "用户名不能为空");
        }

        if (StringUtils.isEmpty(password)) {
            return ResponseUtils.error(ResponseCode.LoginError.LOST_PWD, "密码不能为空");
        }

        UserEntity entry = userDao.findByAccount(account);
        if (entry == null) {
            return ResponseUtils.error(ResponseCode.LoginError.USER_NOT_EXSIT, "用户不存在");
        }

//        String encodedPwd = SaltEncoder.md5SaltEncode(account, password);
        if (!password.equals(entry.getPassword())) {
            return ResponseUtils.error(ResponseCode.LoginError.PWD_ERROR, "密码错误");
        }

        // 判断用户是否重复登录
//        if (entry.isLogin()) {
//            return ResponseUtils.error(ResponseCode.LoginError.USER_LOGINED, "用户已经登录,不可重复登录");
//        }

        // 考试模式下是否已经提交答卷
//        if (entry.isSubmit()) {
//            return ResponseUtils.error(ResponseCode.ExamError.EXAM_COMMITED, "试卷已经提交");
//        }

        Integer userId = entry.getId();

        String tokenStr = getOrCreateToken(userId);

        // 记录login状态
        entry.setLogin(true);
        userDao.update(entry);

        // 登录成功
        return ResponseUtils.success(tokenStr);
    }

    /**
     * 交卷退出
     * @param request
     * @return
     */
    @RequestMapping("/finish")
    @ResponseBody
    public String finish(HttpServletRequest request) {
        String token = request.getHeader("token");

        try {
            TokenEntity tokenEntity = tokenDao.find(token);
            UserEntity userEntity = userDao.findById(tokenEntity.getUserId());
            userEntity.setLogin(false);
            userEntity.setSubmit(true);
            userDao.update(userEntity);
            return ResponseUtils.success("交卷成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error(ResponseCode.ExamError.SUBMIT_FAILD, "提交考试数据失败");
        }

    }


    /**
     * 重置账户状态
     * @param request
     * @return
     */
    @RequestMapping("/reset")
    @ResponseBody
    public String reset(HttpServletRequest request) {
        String token = request.getHeader("token");

        try {
            TokenEntity tokenEntity = tokenDao.find(token);
            UserEntity entry = userDao.findById(tokenEntity.getUserId());
            entry.setLogin(false);
            entry.setSubmit(false);
            userDao.update(entry);

            return ResponseUtils.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error(ResponseCode.LoginError.USER_NOT_EXSIT);
        }
    }


    private String getOrCreateToken(Integer userId) {
        String tokenStr = null;
        TokenEntity tokenEntity = tokenDao.findActive(userId);
        if(tokenEntity != null){
            long timeDuration = new Date().getTime() - tokenEntity.getCreateDate().getTime();
            if(timeDuration - tokenEntity.getDuration() < 0){
                // token存在并在有效期
                tokenStr = tokenEntity.getToken();
            }
        }

        if(tokenStr == null){ // Token不存在, 或已过有效期
            // 生成唯一的token, 添加到token表
            TokenEntity token = new TokenEntity();
            token.setUserId(userId);
            // 保存token
            tokenStr = UUID.randomUUID().toString();
            token.setToken(tokenStr);
            token.setState(TokenEntity.STATE_ENABLE);
            token.setCreateDate(new Date());
            token.setDuration(7 * ONE_DAY); // 默认七天内不需要重复登录
            tokenDao.add(token);
        }
        return tokenStr;
    }
}
