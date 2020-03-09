package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @GetMapping("/notification")
    @ResponseBody
    public String subscribe() {
        return "Subscribed to entity with subscription id";
    }
    @PostMapping("/notification")
    @ResponseBody
	public String handleNotification(@RequestParam(value = "validationToken", defaultValue = "") String validationToken, @RequestBody(required=false) ChangeNotificationsCollection notifications) {
        
		return validationToken;
	}
}
