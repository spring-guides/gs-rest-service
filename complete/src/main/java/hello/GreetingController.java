package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final Logger log = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private WebClient client;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        log.info("Handling home");
        log.info("Handling home111");
        log.info("Handling home222");

        LoggerFactory.getLogger("hoge").info("AAAAAAA");

        System.out.println("=================");

        Mono<String> result = client.get()
                .uri("http://127.0.0.1:9999/?a=b")
                .retrieve().bodyToMono(String.class);

        String response = result.block();
        System.out.println(response);
        System.out.println("=================");

        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
