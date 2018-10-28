package org.service;

import org.dto.Exposer;
import org.dto.SeckillExecution;
import org.entity.Seckill;
import org.exception.RepeatKillException;
import org.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test//完整测试代码
    public void exportSeckillLogic() {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long phone = 13532434839L;
            String md5 = "426d2b2da0dad106902f417857ce1be9";
            try {
                SeckillExecution execution = seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}",execution);
            }catch (RepeatKillException e1){
                logger.error(e1.getMessage());
            }catch (SeckillCloseException e2){
                logger.error(e2.getMessage());
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }


    }

//    @Test
//    public void executeSeckill() {
//        long id = 1000;
//        long phone = 13532434839L;
//        String md5 = "426d2b2da0dad106902f417857ce1be9";
//        try {
//            SeckillExecution execution = seckillService.executeSeckill(id,phone,md5);
//            logger.info("result={}",execution);
//        }catch (RepeatKillException e1){
//            logger.error(e1.getMessage());
//        }catch (SeckillCloseException e2){
//            logger.error(e2.getMessage());
//        }
//        /**
//         * result=SeckillExecution{
//         * seckillId=1000,
//         * state=1,
//         * stateInfo='秒杀成功',
//         * successKilled=SuccessKilled{seckillId=1000,
//         * userPhone=13532434839,
//         * state=0,
//         * createTime=Tue Oct 23 10:42:32 CST 2018
//         */
//    }
}