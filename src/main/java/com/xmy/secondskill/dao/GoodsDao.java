package com.xmy.secondskill.dao;import com.xmy.secondskill.entity.Goods;import com.xmy.secondskill.vo.GoodsVo;import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;/** * @author xmy * @date 2021/3/27 4:26 下午 */@Mapperpublic interface GoodsDao {//    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")    public List<GoodsVo> listGoodsVo();    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") Long goodsId);    //减少库存    public int reduceGoods(@Param("goodsId") long goodsId);    //下面两个是测试//    @Select("select id from goods")    List<String> getAllGoods();    List<Goods> getTestGoods();}