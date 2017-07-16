package fr.sayasoft.fake.zinc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FakeZincController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(value = "/order/{request_id}", method = RequestMethod.GET,)
    public String getOrder(@PathVariable(value = "request_id") String requestId) {
        return null;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<?> postOrder(@RequestBody Object requestBody) {
        return null;
    }
}