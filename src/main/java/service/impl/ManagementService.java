package service.impl;

import controller.EmailJob;
import controller.ParamsRo;
import data.BotConfigRepository;
import data.vo.BotConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.BotRo;
import ro.BotTypeInfoRo;
import service.IManagementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ivan on 18.03.17.
 */

@Service
public class ManagementService implements IManagementService {

    @Autowired
    private BotConfigRepository botConfigRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public BotTypeInfoRo botTypeInfo() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", String.class.getCanonicalName());
        map.put("cron", String.class.getCanonicalName());
        map.put("email", String.class.getCanonicalName());
        map.put("emailpassword", String.class.getCanonicalName());
        map.put("microsoftid", String.class.getCanonicalName());
        map.put("microsoftsecret", String.class.getCanonicalName());
        map.put("emailserver", String.class.getCanonicalName());
        map.put("skypeid", String.class.getCanonicalName());
        BotTypeInfoRo botTypeInfoRo = new BotTypeInfoRo();
        botTypeInfoRo.setParams(map);
        botTypeInfoRo.setName("SKYPE");
        botTypeInfoRo.setDescription("none");
        return botTypeInfoRo;
    }

    @Override
    @Transactional
    public void changeBotParams(ParamsRo paramsRo) {
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

    @Override
    @Transactional
    public void startBot(String botId) {
        try {
            BotConfig botConfig = botConfigRepository.findOne(botId);
            if (botConfig != null && !botConfig.getRunned()) {
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(botId).withSchedule(CronScheduleBuilder.cronSchedule(botConfig.getCron())).build();
                JobDetail job = JobBuilder.newJob(EmailJob.class).withIdentity(botId).build();
                job.getJobDataMap().put("email", botConfig.getEmail());
                job.getJobDataMap().put("emailpassword", botConfig.getEmailpassword());
                job.getJobDataMap().put("emailserver", botConfig.getEmailserver());
                job.getJobDataMap().put("microsoftid", botConfig.getMicrosoft_id());
                job.getJobDataMap().put("microsoftsecret", botConfig.getMicrosoft_secret());
                job.getJobDataMap().put("skypeid", botConfig.getSkype_id());
                this.schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
                botConfig.setRunned(true);
            } else {
                throw new RuntimeException("Bot with id = " + botId + " already runned");
            }
        } catch (Exception e) {
            throw new RuntimeException("Bot with id = " + botId + " already runned");
        }
    }

    @Override
    @Transactional
    public void stopBot(String botId) {
        try {
            BotConfig botConfig = botConfigRepository.findOne(botId);
            if (botConfig != null && botConfig.getRunned()) {
                this.schedulerFactoryBean.getScheduler().unscheduleJob(TriggerKey.triggerKey(botId));
                this.schedulerFactoryBean.getScheduler().deleteJob(JobKey.jobKey(botId));
                botConfig.setRunned(false);
            } else {
                throw new RuntimeException("Bot with id = " + botId + " already stopped");
            }
        } catch (Exception e) {
            throw new RuntimeException("Bot with id = " + botId + " already stopped");
        }
    }

    @Override
    public List<BotRo> getBots(String account_id) {
        List<BotConfig> botConfigs = botConfigRepository.findByAccountId(account_id);
        List<BotRo> botRos = new ArrayList<BotRo>();
        for (BotConfig botConfig: botConfigs) {
            BotRo botRo = new BotRo();
            botRo.setType("SKYPE");
            botRo.setName(botConfig.getName());
            botRo.setId(botConfig.getId());
            botRo.setParams(new HashMap<String, String>());
            botRos.add(botRo);
        }
        return botRos;
    }

    @Override
    public BotRo getBot(String botId) {
        return new BotRo();
    }

    @Override
    public void deleteBot(String botId) {

    }

    @Override
    public BotRo createBot(String name, String account_id) {
        return new BotRo();
    }

    @Override
    public Boolean checkAccess(String botId, String account_id) {
        return true;
    }
}
