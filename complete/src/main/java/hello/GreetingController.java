package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    private final String template;
    private final String defaultName;
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public GreetingController(
            @Value("${template:Hello, %s!}") String template,
            @Value("${defaultName:World}") String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }

    @RequestMapping("/greeting")
    public @ResponseBody Greeting greeting(
            @RequestParam(required=false) String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name != null ? name : defaultName));
    }
}