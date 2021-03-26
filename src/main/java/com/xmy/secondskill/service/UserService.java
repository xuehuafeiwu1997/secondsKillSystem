package com.xmy.secondskill.service;import com.xmy.secondskill.dao.UserDao;import com.xmy.secondskill.entity.User;import com.xmy.secondskill.result.CodeMsg;import com.xmy.secondskill.result.Result;import com.xmy.secondskill.util.MD5Util;import com.xmy.secondskill.vo.LoginVo;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.ResponseBody;import javax.servlet.http.HttpServletResponse;/** * @author xmy * @date 2021/3/23 9:50 上午 */@Servicepublic class UserService {    @Autowired    private UserDao userDao;//    public User getById(int id) {//        return userDao.getById(id);//    }    //让事务起作用，下面两次插入操作当成一个，要么都成功，要么都不成功,不加注解，虽然程序会报错，但是id为2的数据可以插进去    //让事务起作用需要加注解//    @Transactional//    public boolean tx() {//        User u1 = new User();//        u1.setId(2);//        u1.setName("222222");//        //插入没问题//        userDao.insert(u1);////        User u2 = new User();//        u2.setId(1);//        u2.setName("1111111");//        //插入有问题//        userDao.insert(u2);//        return true;//    }//    public boolean login(HttpServletResponse, LoginVo loginVo) {//        if (loginVo == null) {//            throw  new//        }//    }    public User findMobileAndPasswordById(Long id) {        return userDao.findMobileAndPasswordById(id);    }    //登录方法    public CodeMsg login(LoginVo loginVo) {        if (loginVo == null) {            return CodeMsg.SERVER_ERROR;        }        String mobile = loginVo.getMobile();        String formPass = loginVo.getPassword();        //判断手机号是否存在        User user = findMobileAndPasswordById(Long.parseLong(mobile));        if (user == null) {            return CodeMsg.MOBILE_NOT_EXIST;        }        //验证密码        String dbPass = user.getPassword();        String saltDB = user.getSalt();        String calcPass = MD5Util.formPassToDBPass(formPass,saltDB);        if (!calcPass.equals(dbPass)) {            return CodeMsg.PASSWORD_ERROR;        }        return CodeMsg.SUCCESS;    }}