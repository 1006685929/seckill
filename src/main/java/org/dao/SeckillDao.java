package org.dao;

import org.apache.ibatis.annotations.Param;
import org.entity.Seckill;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;


public interface SeckillDao {

    //减库存
    //java中没有保存形参的记录，用@Param解决参数识别
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    //根据id查询秒杀对象
    Seckill queryById(long seckillId);

    //根据偏移量查询秒杀商品
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}
