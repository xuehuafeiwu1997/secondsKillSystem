package com.xmy.secondskill.controller;import com.sun.org.apache.xpath.internal.operations.Bool;import com.xmy.secondskill.dao.UserDao;import com.xmy.secondskill.result.CodeMsg;import com.xmy.secondskill.result.Result;import com.xmy.secondskill.service.UserService;import com.xmy.secondskill.util.ValidatorUtil;import com.xmy.secondskill.vo.LoginVo;import org.apache.commons.lang3.StringUtils;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.ResponseBody;import javax.servlet.http.HttpServletResponse;import javax.validation.Valid;/** * @author xmy * @date 2021/3/24 9:53 下午 */@Controller@RequestMapping("/login")public class LoginController {    @Autowired    private UserDao userDao;    @Autowired    private UserService userService;    private static Logger log = LoggerFactory.getLogger(LoginController.class);    @GetMapping("/to_login")    public String toLogin() {        return "login";    }    @PostMapping("/do_login")    @ResponseBody    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {        log.info(loginVo.toString());        //使用jsr303校验，将这部分手写的验证代码注销//        //参数校验//        String passInput = loginVo.getPassword();//        String mobile = loginVo.getMobile();//        if (StringUtils.isEmpty(passInput)) {//            return Result.error(CodeMsg.PASSWORD_ERROR);//        }//        if (StringUtils.isEmpty(mobile)) {//            return Result.error(CodeMsg.MOBILE_EMPTY);//        }//        if (!ValidatorUtil.isMobile(mobile)) {//            return Result.error(CodeMsg.MOBILE_ERROR);//        }        //登录        CodeMsg cm = userService.login(response,loginVo);        if (cm.getCode() == 0) {            return Result.success(true);        } else {            return Result.error(cm);        }    }}