package com.xmy.secondskill.service;import com.xmy.secondskill.dao.UserDao;import com.xmy.secondskill.entity.User;import com.xmy.secondskill.result.Result;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.ResponseBody;/** * @author xmy * @date 2021/3/23 9:50 上午 */@Servicepublic class UserService {    @Autowired    private UserDao userDao;    public User getById(int id) {        return userDao.getById(id);    }    //让事务起作用，下面两次插入操作当成一个，要么都成功，要么都不成功,不加注解，虽然程序会报错，但是id为2的数据可以插进去    //让事务起作用需要加注解//    @Transactional    public boolean tx() {        User u1 = new User();        u1.setId(2);        u1.setName("222222");        //插入没问题        userDao.insert(u1);        User u2 = new User();        u2.setId(1);        u2.setName("1111111");        //插入有问题        userDao.insert(u2);        return true;    }}