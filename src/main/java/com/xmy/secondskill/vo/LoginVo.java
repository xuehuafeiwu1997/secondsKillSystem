package com.xmy.secondskill.vo;/** * @author xmy * @date 2021/3/24 10:08 下午 */public class LoginVo {    private String mobile;    private String password;    public String getMobile() {        return mobile;    }    public void setMobile(String mobile) {        this.mobile = mobile;    }    public String getPassword() {        return password;    }    public void setPassword(String password) {        this.password = password;    }    @Override    public String toString() {        return "LoginVo{" +                "mobile='" + mobile + '\'' +                ", password='" + password + '\'' +                '}';    }}