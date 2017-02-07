package service;

import controller.EmailJob;
import controller.ParamsRo;
import data.BotConfigRepository;
import data.vo.BotConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by asus-pc on 04.02.2017.
 */

@Service
public class EmailSkypeService {

    @Autowired
    private BotConfigRepository botConfigRepository;

    @Autowired
    private org.springframework.scheduling.quartz.SchedulerFactoryBean schedulerFactoryBean;

    @Transactional
    public void updateBotConfig(ParamsRo paramsRo) {
        BotConfig botConfig = botConfigRepository.findOne(paramsRo.getId());
        if (botConfig == null) {
            botConfig = new BotConfig();
            botConfig.setId(paramsRo.getId());
            botConfig.setRunned(false);
        }

        if (botConfig != null && !botConfig.getRunned()) {
            if (paramsRo.getParams().get("name") != null)
                botConfig.setName(paramsRo.getParams().get("name"));
            else
                throw new RuntimeException("Parameter is null");
            if (paramsRo.getParams().get("email") != null)
                botConfig.setEmail(paramsRo.getParams().get("email"));
            else
                throw new RuntimeException("Parameter is null");
            if (paramsRo.getParams().get("cron") != null)
                botConfig.setCron(paramsRo.getParams().get("cron"));
            else
                throw new RuntimeException("Parameter is null");
            if (paramsRo.getParams().get("emailpassword") != null)
                botConfig.setEmailpassword(paramsRo.getParams().get("emailpassword"));
            else
                throw new RuntimeException("Parameter is null");
            if (paramsRo.getParams().get("microsoftid") != null)
                botConfig.setMicrosoft_id(paramsRo.getParams().get("microsoftid"));
            else
                throw new RuntimeException("Parameter is null");
            if (paramsRo.getParams().get("microsoftsecret") != null)
                botConfig.setMicrosoft_secret(paramsRo.getParams().get("microsoftsecret"));
            else
                throw new RuntimeException("Parameter is null");
            if (paramsRo.getParams().get("emailserver") != null)
                botConfig.setEmailserver(paramsRo.getParams().get("emailserver"));
            else
                throw new RuntimeException("Parameter is null");
            if (paramsRo.getParams().get("skypeid") != null)
                botConfig.setSkype_id(paramsRo.getParams().get("skypeid"));
            else
                throw new RuntimeException("Parameter is null");
            botConfig.setRunned(false);
            botConfigRepository.save(botConfig);
        } else {
            throw new RuntimeException("Bot with id = " + paramsRo.getId() + " already runned");
        }
    }

    @Transactional
    public void start(String id) throws SchedulerException {
        BotConfig botConfig = botConfigRepository.findOne(id);
        if (botConfig != null && !botConfig.getRunned()) {
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(id).withSchedule(CronScheduleBuilder.cronSchedule(botConfig.getCron())).build();
            JobDetail job = JobBuilder.newJob(EmailJob.class).withIdentity(id).build();
            job.getJobDataMap().put("email", botConfig.getEmail());
            job.getJobDataMap().put("emailpassword", botConfig.getEmailpassword());
            job.getJobDataMap().put("emailserver", botConfig.getEmailserver());
            job.getJobDataMap().put("microsoftid", botConfig.getMicrosoft_id());
            job.getJobDataMap().put("microsoftsecret", botConfig.getMicrosoft_secret());
            job.getJobDataMap().put("skypeid", botConfig.getSkype_id());
            this.schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
            botConfig.setRunned(true);
        } else {
            throw new RuntimeException("Bot with id = " + id + " already runned");
        }
    }

    @Transactional
    public void stop(String id) throws SchedulerException {
        BotConfig botConfig = botConfigRepository.findOne(id);
        if (botConfig != null && botConfig.getRunned()) {
            this.schedulerFactoryBean.getScheduler().unscheduleJob(TriggerKey.triggerKey(id));
            this.schedulerFactoryBean.getScheduler().deleteJob(JobKey.jobKey(id));
            botConfig.setRunned(false);
        } else {
            throw new RuntimeException("Bot with id = " + id + " already stopped");
        }
    }


}
