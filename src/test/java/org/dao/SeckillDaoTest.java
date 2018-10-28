package org.dao;

import org.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {


    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long seckillId=1000;
        Seckill seckill=seckillDao.queryById(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }


    @Test
    public void queryAll() throws Exception{
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill seckill : seckills){
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception{

        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println("updateCount"+updateCount);
    }


}