package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Client {
    private final Logger log = LoggerFactory.getLogger(GreetingController.class);

    @Bean
    public WebClient getClient() {
        return WebClient.builder()
                .filter(logRequest()).build();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            System.out.println("**************");
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            System.out.println("**************");
            return next.exchange(clientRequest);
        };
    }
}
