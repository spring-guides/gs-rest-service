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

    @RequestMapping(value = "/verifyMail", method = RequestMethod.POST)
    public boolean verifyMail(@RequestParam String mail) {
        if(mail.length() < 5) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/verifyNIP", method = RequestMethod.POST)
    public boolean verifyNIP(@RequestParam Long nip) {
        if (String.valueOf(nip).length() < 10) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/verifyPESEL", method = RequestMethod.POST)
    public boolean verifyPESEL(@RequestParam Long pesel) {
        if (String.valueOf(pesel).length() != 11) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/verifyREGON", method = RequestMethod.POST)
    public boolean verifyREGON(@RequestParam Long regon) {
        if (String.valueOf(regon).length() == 9 || String.valueOf(regon).length() == 14) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/verifyPostcode", method = RequestMethod.POST)
    public boolean verifyPostcode(@RequestParam String postcode) {
        if (postcode.length() == 6) {
            return true;
        }
        return false;
    }
}
