import config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by asus-pc on 04.02.2017.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = { WebConfig.class })
public class EmailBot extends SpringBootServletInitializer {

    private static ApplicationContext ctx;

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

}
