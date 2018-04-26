package org.com.coolfish;

import org.com.coolfish.common.database.entity.KuyuFlowDetail;
import org.com.coolfish.common.database.service.KuyuFlowDetailService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    private KuyuFlowDetailService kuyuFlowDetailService;

    @org.junit.Test
    public void testFlowDetail() {
        KuyuFlowDetail flowDetail = kuyuFlowDetailService.findLastRecord("1064961585471");
        System.out.println(flowDetail.toString());
    }

}
