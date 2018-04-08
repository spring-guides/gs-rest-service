package zad2ppkwu;

import javafx.scene.control.Hyperlink;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Justyna on 08.04.2018.
 */

@RestController
public class FormController {

    @RequestMapping(value = "/fields", method = RequestMethod.POST)
    public Fields fields(@RequestParam Fields fields) {
        return new Fields(1, "jankowalski@example.com", Long.parseLong("0123456789"), 12123423456L, Long.parseLong("098472839"), "01-100");
    }
}
