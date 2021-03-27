package com.xmy.secondskill.controller;import com.xmy.secondskill.entity.User;import com.xmy.secondskill.redis.MiaoShaUserKey;import com.xmy.secondskill.redis.RedisService;import com.xmy.secondskill.service.UserService;import org.apache.commons.lang3.StringUtils;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.CookieValue;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;import javax.servlet.http.HttpServletResponse;/** * @author xmy * @date 2021/3/26 9:24 下午 */@Controller@RequestMapping("/goods")public class GoodsController {    @Autowired    UserService userService;    @Autowired    RedisService redisService;    @RequestMapping("/to_list")    public String list(HttpServletResponse response,Model model, User user) {        model.addAttribute("user",user);        return "goods_list";    }    //不使用argumentResolver时，每一个界面都需要像下面这样获取token值，并且还要判断是否为空，代码特别繁琐，使用argumentResolver时，就会使代码简洁很多    @RequestMapping("/to_detail")    public String detail(HttpServletResponse response,Model model, @CookieValue(value = UserService.COOKIE_NAME_TOKEN,required = false) String cookieToken, @RequestParam(value = UserService.COOKIE_NAME_TOKEN,required = false) String paramToken) {        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {            //如果两个地方的cookie都没有，则直接返回登录界面            return "login";        }        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;        User user = userService.getByToken(response,token);        model.addAttribute("user",user);        return "goods_list";    }}