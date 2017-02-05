package controller;

import data.BotConfigRepository;
import data.vo.BotConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.EmailSkypeService;

/**
 * Created by asus-pc on 04.02.2017.
 */

@RestController
@RequestMapping(value = "/management")
public class ManagementController {

    @Autowired
    private org.springframework.scheduling.quartz.SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private BotConfigRepository botConfigRepository;

    @Autowired
    private EmailSkypeService emailSkypeService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void start(@RequestParam String id) throws SchedulerException {
        BotConfig botConfig = botConfigRepository.findOne(id);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(id).withSchedule(CronScheduleBuilder.cronSchedule(botConfig.getCron())).build();
        JobDetail job = JobBuilder.newJob(EmailJob.class).withIdentity(id).build();
        job.getJobDataMap().put("email", botConfig.getEmail());
        job.getJobDataMap().put("emailpassword", botConfig.getEmailpassword());
        job.getJobDataMap().put("emailserver", botConfig.getEmailserver());
        job.getJobDataMap().put("microsoftid", botConfig.getMicrosoft_id());
        job.getJobDataMap().put("microsoftsecret", botConfig.getMicrosoft_secret());
        this.schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public void stop(@RequestParam String id) throws SchedulerException {
        this.schedulerFactoryBean.getScheduler().unscheduleJob(TriggerKey.triggerKey(id));
        this.schedulerFactoryBean.getScheduler().deleteJob(JobKey.jobKey(id));
    }

    @RequestMapping(value = "/changeParams", method = RequestMethod.POST, produces = "application/json")
    public void changeParams(@RequestBody ParamsRo paramsRo) {
        BotConfig botConfig = botConfigRepository.findOne(paramsRo.getId());
        if (botConfig == null) {
            botConfig = new BotConfig();
            botConfig.setId(paramsRo.getId());
        }
        botConfig.setName(paramsRo.getParams().get("name"));
        botConfig.setEmail(paramsRo.getParams().get("email"));
        botConfig.setCron(paramsRo.getParams().get("cron"));
        botConfig.setEmailpassword(paramsRo.getParams().get("emailpassword"));
        botConfig.setMicrosoft_id(paramsRo.getParams().get("microsoftid"));
        botConfig.setMicrosoft_secret(paramsRo.getParams().get("microsoftsecret"));
        botConfig.setEmailserver(paramsRo.getParams().get("emailserver"));
        emailSkypeService.updateBotConfig(botConfig);
    }

}
