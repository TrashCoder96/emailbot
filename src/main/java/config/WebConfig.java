package config;

import controller.ManagementController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import service.EmailSkypeService;

/**
 * Created by asus-pc on 04.02.2017.
 */

@Configuration
@ComponentScan(basePackageClasses = { EmailSkypeService.class, ManagementController.class })
public class WebConfig {

    @Bean
    public org.springframework.scheduling.quartz.SchedulerFactoryBean schedulerFactoryBean() {
        return new org.springframework.scheduling.quartz.SchedulerFactoryBean();
    }

}
