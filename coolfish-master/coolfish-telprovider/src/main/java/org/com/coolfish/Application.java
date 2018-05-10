package org.com.coolfish;

import org.com.coolfish.scheduler.EmptyScheduler;
import org.com.coolfish.scheduler.EveryDayFlowWriterScheduler;
import org.com.coolfish.scheduler.MonthlyScheduler;
import org.com.coolfish.scheduler.SilentScheduler;
import org.com.coolfish.scheduler.StartMonthlyHaveSumFlowScheduler;
import org.com.coolfish.scheduler.StopMonthlyNullSumFlow;
import org.com.coolfish.scheduler.UpdateAddpackageEndTime;
import org.com.coolfish.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private EmptyScheduler msisdnEmptyScheduler;

    @Autowired
    private MonthlyScheduler msisdnMonthlyScheduler;

    @Autowired
    private SilentScheduler msisdnSilentScheduler;

    @Autowired
    private StopMonthlyNullSumFlow stopMonthlyNullSumFlow;

    @Autowired
    private StartMonthlyHaveSumFlowScheduler startMonthlyHaveSumFlowScheduler;

    @Autowired
    private ActionService actionService;

    @Autowired
    private UpdateAddpackageEndTime updateAddpackageEndTime;

    @Autowired
    private EveryDayFlowWriterScheduler everyDayFlowWriterScheduler;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始执行计划");
    }

}
