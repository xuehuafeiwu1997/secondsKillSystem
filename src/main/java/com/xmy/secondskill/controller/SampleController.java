package com.xmy.secondskill.controller;import com.xmy.secondskill.entity.User;import com.xmy.secondskill.redis.RedisService;import com.xmy.secondskill.redis.UserKey;import com.xmy.secondskill.result.Result;import com.xmy.secondskill.service.UserService;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.ResponseBody;/** * @author xmy * @date 2021/3/22 4:35 下午 */@Controllerpublic class SampleController {    @Autowired    private UserService userService;    @Autowired    private RedisService redisService;    @GetMapping("/hello")    public String hello(Model model) {        model.addAttribute("name","许明洋");        return "hello";    }//    @ResponseBody//    @GetMapping("/db/get")//    public String dbGet() {//        User user = userService.getById(1);//        return user.getName();//    }//    @ResponseBody//    @GetMapping("/db/tx")//    public String dbTx() {//        boolean flag = userService.tx();//        return flag ? "success" : "failed";//    }//    @RequestMapping("/redis/get")//    @ResponseBody//    public Result<User> redisGet() {//        User user = redisService.get(UserKey.getById,""+1,User.class);//        return Result.success(user);//    }////    @RequestMapping("/redis/set")//    @ResponseBody//    public Result<Boolean> redisSet() {//        User user = new User();//        user.setId(1);//        user.setName("许明洋");//        redisService.set(UserKey.getById,""+1,user);//        return Result.success(true);//    }}