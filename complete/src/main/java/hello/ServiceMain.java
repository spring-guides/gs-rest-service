package hello;

import org.springframework.bootstrap.SpringApplication;
import org.springframework.bootstrap.context.annotation.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class ServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMain.class, args);
    }
}
