import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.WebConfig;
import controller.EmailJob;
import data.BotConfigRepository;
import data.vo.BotConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by asus-pc on 04.02.2017.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = { WebConfig.class })
public class EmailBot extends SpringBootServletInitializer {

    @Value("${service.host}")
    private String mainhost;

    @Value("${service.port}")
    private String mainport;

    @Value("${server.name}")
    private String name;

    @Value("${server.port}")
    private String port;

    @Value("${server.host}")
    private String host;

    private static ApplicationContext ctx;

    @Autowired
    private BotConfigRepository botConfigRepository;

    @Autowired
    private org.springframework.scheduling.quartz.SchedulerFactoryBean schedulerFactoryBean;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EmailBot.class);
    }

    public static void main(String[] args) {
        ctx = SpringApplication.run(EmailBot.class, args);
    }

    public static ApplicationContext getCtx() {
        return ctx;
    }

    @PostConstruct
    public void post() throws SchedulerException, IOException {
        for (BotConfig botConfig: botConfigRepository.findAll()) {
            if (botConfig.getRunned()) {
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(botConfig.getId()).withSchedule(CronScheduleBuilder.cronSchedule(botConfig.getCron())).build();
                JobDetail job = JobBuilder.newJob(EmailJob.class).withIdentity(botConfig.getId()).build();
                job.getJobDataMap().put("email", botConfig.getEmail());
                job.getJobDataMap().put("emailpassword", botConfig.getEmailpassword());
                job.getJobDataMap().put("emailserver", botConfig.getEmailserver());
                job.getJobDataMap().put("microsoftid", botConfig.getMicrosoft_id());
                job.getJobDataMap().put("microsoftsecret", botConfig.getMicrosoft_secret());
                job.getJobDataMap().put("skypeid", botConfig.getSkype_id());
                this.schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
            }
        }
        RegRo regRo = new RegRo();
        regRo.setName(name);
        regRo.setPort(port);
        regRo.setHost(host);
        request(regRo);
    }

    private void request(RegRo regRo) throws IOException {
        String url = "http://" + mainhost + ":" + mainport + "/register";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(regRo);

        String content = json;

        post.setEntity(new ByteArrayEntity(content.getBytes("UTF-8")));
        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
    }
}
