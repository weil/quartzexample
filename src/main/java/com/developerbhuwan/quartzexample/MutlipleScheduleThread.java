/*
 * Date : 2015-07-06
 * Author : Bhuwan Prasad Upadhyay
 * Website : http://www.bhuwanupadhyay.com.np
 */
package com.developerbhuwan.quartzexample;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author developerbhuwan
 */
public class MutlipleScheduleThread {

    public static void main(String[] args) {
        new MutlipleScheduleThread().createMultipleThread();
    }

    private void createMultipleThread() {
        for (int i = 0; i < 10; i++) { //create 10 scheduler thread to perform different job on the basis of param
            try {
                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put(MyJob.KEY_PARAM, "ParamValue");
                JobKey jobKey = new JobKey("UniqueJobId" + i, "JobGroup");
                JobDetail job = JobBuilder
                        .newJob(MyJob.class)
                        .withIdentity(jobKey)
                        .setJobData(jobDataMap)
                        .storeDurably(true)
                        .build();
                TriggerKey triggerKey = new TriggerKey("UniqueTriggerID" + i, "TriggerGroup");
                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/1 1/1 * ? *")) //every one hour
                        .forJob(job)
                        .build();
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException ex) {
                Logger.getLogger(MutlipleScheduleThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static class MyJob implements Job {

        private static String KEY_PARAM = "param";

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String param = dataMap.getString(KEY_PARAM);
            //do job on the basis of parameter
        }
    }
}
