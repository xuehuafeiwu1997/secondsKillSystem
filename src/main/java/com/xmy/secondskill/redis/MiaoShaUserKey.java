package com.xmy.secondskill.redis;/** * @author xmy * @date 2021/3/26 9:12 下午 */public class MiaoShaUserKey extends BasePrefix {    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;    private MiaoShaUserKey(int expireSeconds,String prefix) {        super(expireSeconds,prefix);    }    public static MiaoShaUserKey token = new MiaoShaUserKey(TOKEN_EXPIRE,"tk");    public static MiaoShaUserKey getById = new MiaoShaUserKey(0, "id");}