package org.service.impl;

import org.dao.SeckillDao;
import org.dao.SuccessKilledDao;
import org.dto.Exposer;
import org.dto.SeckillExecution;
import org.entity.Seckill;
import org.entity.SuccessKilled;
import org.enums.SeckillStatEnum;
import org.exception.RepeatKillException;
import org.exception.SeckillCloseException;
import org.exception.SeckillException;
import org.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by hxk
 * 2018/10/22 9:23
 * 接口具体实现
 */

@Service
public class SeckillServiceImpl implements SeckillService {

    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //dao配合,注入Service依赖
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    //md5盐值字符串，用于混淆MD5
    private final String slat = "453dggderwwiovmgr&*(#*#()#";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = getById(seckillId);
        if (seckill == null){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime =new Date();
        if (nowTime.getTime()<startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMD5(seckillId);//md5就是可以把任意一个字符串转换成一种特定长度编码，不可逆
        return new Exposer(true,md5,seckillId);
    }

    //md5生成方法
    private String getMD5(long seckillId){
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional//使用注解控制事务
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存  +   记录购买行为
        Date nowTime = new Date();
        try {
            //减库存
            int updataCount = seckillDao.reduceNumber(seckillId,nowTime);
            if (updataCount <= 0) {
                //没有更新记录  秒杀结束
                throw new SeckillCloseException("seckill is close");
            }else {
                //减库存成功，记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if (insertCount<=0){
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                }else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译异常转化为运行时异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }

    }
}
