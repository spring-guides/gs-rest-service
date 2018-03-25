package hello;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Justyna on 08.03.2018.
 */

@RestController
public class GreetingController {

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(13L, name);
    }

    @RequestMapping(value = "/rev", method = RequestMethod.GET)
    public String revString(@RequestParam(value = "revString", defaultValue = "world") String revString) {
        return new StringBuilder(revString).reverse().toString();
    }
}
