package org.dao;

import org.apache.ibatis.annotations.Param;
import org.entity.SuccessKilled;

public interface SuccessKilledDao {

    //成功秒杀后插入购买明细，过滤重复
    //如果影响行数>1，表示更新的记录行数
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    //根据id查询SuccessKilled并返回秒杀产品对象实体
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
