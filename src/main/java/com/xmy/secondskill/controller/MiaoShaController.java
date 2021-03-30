package com.xmy.secondskill.controller;import com.sun.tools.javac.jvm.Code;import com.xmy.secondskill.entity.MiaoshaOrder;import com.xmy.secondskill.entity.OrderInfo;import com.xmy.secondskill.entity.User;import com.xmy.secondskill.rabbitmq.MQSender;import com.xmy.secondskill.rabbitmq.MiaoShaMessage;import com.xmy.secondskill.redis.GoodsKey;import com.xmy.secondskill.redis.MiaoshaKey;import com.xmy.secondskill.redis.OrderKey;import com.xmy.secondskill.redis.RedisService;import com.xmy.secondskill.result.CodeMsg;import com.xmy.secondskill.result.Result;import com.xmy.secondskill.service.GoodsService;import com.xmy.secondskill.service.MiaoShaService;import com.xmy.secondskill.service.OrderService;import com.xmy.secondskill.vo.GoodsVo;import org.springframework.beans.factory.InitializingBean;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestMethod;import org.springframework.web.bind.annotation.RequestParam;import org.springframework.web.bind.annotation.ResponseBody;import java.util.HashMap;import java.util.List;import java.util.Map;/** * @author xmy * @date 2021/3/28 12:58 下午 */@Controller@RequestMapping("/miaosha")public class MiaoShaController implements InitializingBean {    @Autowired    private GoodsService goodsService;    @Autowired    private OrderService orderService;    @Autowired    private MiaoShaService miaoShaService;    @Autowired    private RedisService redisService;    @Autowired    private MQSender sender;    private Map<Long,Boolean> localOverMap = new HashMap<Long,Boolean>();    //系统初始化    @Override    public void afterPropertiesSet() throws Exception {        //系统初始化时就将库存数量查询出来，并将库存数量存储到redis中        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();        if (goodsVoList == null) {            return;        }        for (GoodsVo goods : goodsVoList) {            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());            localOverMap.put(goods.getId(),false);        }    }    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)    @ResponseBody    public Result<Integer> list(Model model, User user, @RequestParam("goodsId") Long goodsId) {        model.addAttribute("user",user);        //判断用户是否登录        if (user == null) {            return Result.error(CodeMsg.SESSION_ERROR);        }        //内存标记，减少redis访问        boolean over = localOverMap.get(goodsId);        if (over) {            return Result.error(CodeMsg.MIAO_SHA_OVER);        }        //当进来11个、12个请求的时候，仍然会调用redis减一，使用localOverMap来进行优化        //预减库存        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);        if (stock < 0) {            localOverMap.put(goodsId,true);            return Result.error(CodeMsg.MIAO_SHA_OVER);        }        //判断是否已经秒杀到了        MiaoshaOrder order = orderService.getMiaoShaOrderByUserIdAndGoodsId(user.getId(), goodsId);        if (order != null) {            return Result.error(CodeMsg.REPEATE_MIAOSHA);        }        //入队        MiaoShaMessage mm = new MiaoShaMessage();        mm.setUser(user);        mm.setGoodsId(goodsId);        sender.sendMiaoshaMessage(mm);        return Result.success(0);//0代表排队中        //判断库存//        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);//        int stock = goodsVo.getStockCount();//        if (stock <= 0) {////            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());////            return "miaosha_fail";//            return Result.error(CodeMsg.MIAO_SHA_OVER);//        }//        //判断是否已经秒杀成功//        MiaoshaOrder miaoshaOrder = orderService.getMiaoShaOrderByUserIdAndGoodsId(user.getId(),goodsId);//        if (miaoshaOrder != null) {////            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());////            return "miaosha_fail";//            return Result.error(CodeMsg.REPEATE_MIAOSHA);//        }//        //减少库存，下订单，写入秒杀订单//        OrderInfo orderInfo = miaoShaService.miaosha(user,goodsVo);//        System.out.println(orderInfo.toString());//        model.addAttribute("orderInfo",orderInfo);//        model.addAttribute("goods",goodsVo);//        return "order_detail";//        return Result.success(orderInfo);    }    /**     * orderId:成功     * -1秒杀失败     * 0：排队中     */    @RequestMapping(value = "/result",method = RequestMethod.GET)    @ResponseBody    public Result<Long> miaoshaResult(Model model,User user,@RequestParam("goodsId") long goodsId) {        model.addAttribute("user",user);        if (user == null) {            return Result.error(CodeMsg.SESSION_ERROR);        }        long result = miaoShaService.getMiaoShaResult(user.getId(),goodsId);        return Result.success(result);    }    /**     * 还原所有的数据     */    @RequestMapping(value="/reset",method = RequestMethod.GET)    @ResponseBody    public Result<Boolean> reset(Model model) {        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();        for (GoodsVo goods : goodsVoList) {            goods.setStockCount(10);            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),10);            localOverMap.put(goods.getId(),false);        }        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);        redisService.delete(MiaoshaKey.isGoodsOver);        miaoShaService.reset(goodsVoList);        return Result.success(true);    }}