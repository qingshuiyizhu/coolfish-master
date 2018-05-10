package org.com.coolfish.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAddPackage;
import org.com.coolfish.common.database.service.KuyuAddPackageService;
import org.com.coolfish.common.database.service.KuyuPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UpdateAddpackageEndTime {
    @Autowired
    private KuyuAddPackageService kuyuaddPackageService;

    @Autowired
    private KuyuPackageService kuyuPackageService;

    public void updateAddPackageEndTime() {
        log.info("开始处理沉默期结束时间错误的数据");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
            String dstr = "2020-01-01 00:00:00";

            java.util.Date date = sdf.parse(dstr);
            List<KuyuAddPackage> list = kuyuaddPackageService.findEndTimeError();
            int number = list.size();
            log.info("进行订购了当月套餐的所有号码数量：{}", number);

            for (KuyuAddPackage kuyuAddPackage : list) {
                log.info("剩余未处理号码个数：[{}],正在处理kuyuAddPackageID[{}]数据:{}", number--, kuyuAddPackage.getCardId(),
                        kuyuAddPackage.toString());
                if (null != kuyuAddPackage.getPackageid() && kuyuAddPackage.getPackageid() > 0) {
                    Integer usetime = kuyuPackageService.findUsetime(kuyuAddPackage.getPackageid());
                    if (null != usetime && usetime > 0) {
                        long apartTime = usetime * 24 * 60 * 60 * 1000L;

                        String endTime = sdf.format(new Date(kuyuAddPackage.getStarttime().getTime() + apartTime));

                        log.info("kuyuAddPackageID[{}]开始时间[{}],相隔天数[{}]矫正后结束时间数据:{}",
                                kuyuAddPackage.getCardId(), kuyuAddPackage.getStarttime(), usetime, endTime);

                        kuyuaddPackageService.flushEndTime(kuyuAddPackage.getId(),
                                new Date(kuyuAddPackage.getStarttime().getTime() + apartTime));
                    }
                }

            }

        } catch (ParseException e) {

            e.printStackTrace();
        }

    }

}
