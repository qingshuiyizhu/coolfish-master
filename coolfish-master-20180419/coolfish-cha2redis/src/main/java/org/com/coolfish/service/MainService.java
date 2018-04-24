package org.com.coolfish.service;

import java.util.List;

import org.com.coolfish.entity.KuyuCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MainService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CHARestService restTemplateService;

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private CMCCSRestervice cmccSRestervice;
 
    public void start1() {
        logger.info("---------------------------程序运行开始----------------------------------------------");
        cmccSRestervice.NUllLiuLiangTFJ(118, "109000000114", "IWyDm4ie9o12WEmLJYm0", "51810008262", null);
        cmccSRestervice.NUllLiuLiangTFJ(137, "109000000114", "IWyDm4ie9o12WEmLJYm0", "51810008262", null);
        cmccSRestervice.NUllLiuLiangTFJ(142, "109000000164", "EvJe5WOe4yq3FYqe0eYK", "51235553285", null);
        cmccSRestervice.NUllLiuLiangTFJ(143, "109000000164", "EvJe5WOe4yq3FYqe0eYK", "51235553285", null);
        cmccSRestervice.NUllLiuLiangTFJ(159, "109000000154", "DgRPhzDYP8N5sILANslO", "51734319282", null);
        cmccSRestervice.NUllLiuLiangTFJ(160, "109000000114", "IWyDm4ie9o12WEmLJYm0", "51810008262", null);
        cmccSRestervice.NUllLiuLiangTFJ(167, "109000000114", "IWyDm4ie9o12WEmLJYm0", "51810008262", null);
        cmccSRestervice.NUllLiuLiangTFJ(170, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330158013", null);
        cmccSRestervice.NUllLiuLiangTFJ(171, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330158013", null);
        cmccSRestervice.NUllLiuLiangTFJ(173, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330158013", null);
        cmccSRestervice.NUllLiuLiangTFJ(179, "109000000164", "EvJe5WOe4yq3FYqe0eYK", "51235553285", null);
        cmccSRestervice.NUllLiuLiangTFJ(180, "109000000164", "EvJe5WOe4yq3FYqe0eYK", "51235553285", null);
        cmccSRestervice.NUllLiuLiangTFJ(181, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330158013", null);
        cmccSRestervice.NUllLiuLiangTFJ(182, "109000000164", "EvJe5WOe4yq3FYqe0eYK", "51235553285", null);
        cmccSRestervice.NUllLiuLiangTFJ(183, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330158013", null);
        cmccSRestervice.NUllLiuLiangTFJ(185, "109000000164", "EvJe5WOe4yq3FYqe0eYK", "51235553285", null);
        cmccSRestervice.NUllLiuLiangTFJ(185, "109000000164", "EvJe5WOe4yq3FYqe0eYK", "51235553285", null);
        cmccSRestervice.NUllLiuLiangTFJ(191, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330158013", null);
        cmccSRestervice.NUllLiuLiangTFJ(192, "109000000154", "DgRPhzDYP8N5sILANslO", "51734319282", null);
        cmccSRestervice.NUllLiuLiangTFJ(193, "109000000154", "DgRPhzDYP8N5sILANslO", "51734319282", null);
        cmccSRestervice.NUllLiuLiangTFJ(194, "109000000154", "DgRPhzDYP8N5sILANslO", "51734319282", null);
        cmccSRestervice.NUllLiuLiangTFJ(202, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52310013254", null);

        cmccSRestervice.NUllLiuLiangTFJ(203, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52310013254", null);
        cmccSRestervice.NUllLiuLiangTFJ(204, "109000000154", "DgRPhzDYP8N5sILANslO", "51734319282", null);
        cmccSRestervice.NUllLiuLiangTFJ(210, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330089007", null);
        cmccSRestervice.NUllLiuLiangTFJ(211, "109000000174", "gaW6ZgMpFDsi43FeZJiJ", "52330089007", null);
    }

    public void start() {
        
        List<KuyuCard> list = kuyuCardService.findInA(137);
        for (int i = 0; i < list.size(); i++) {
        //    restTemplateService.queryCardStatus("1064915327521", "20180301", i);
         System.out.println(list.get(i).getTel());
            ;
        }
    }
}
