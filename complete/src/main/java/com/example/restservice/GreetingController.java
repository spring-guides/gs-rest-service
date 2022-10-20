package com.example.restservice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class GreetingController {

	private static final String template = "Hi %s, Served from greeting api. ";
	private final AtomicLong counter = new AtomicLong();

	
	private HttpService httpservice = new HttpService() ;
		
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "User") String name) {
		System.out.println("--------------Greeting------------------------");
		return new Greeting(counter.incrementAndGet(), String.format(template, name)+getGreeting());
	}
	
	@GetMapping("/coffee")
	public String getcoffee() {
		System.out.println("--------------Coffee------------------------");
        Map<String, Object> reqParams = new HashMap<>();
      
        String response = httpservice.makeHttpGetCall("https://api.sampleapis.com/coffee/hot", reqParams , getHttpHeaders(), String.class);
		return response;
	} 
	
	@GetMapping("/cat")
	public String getcat() {
		System.out.println("--------------Cat------------------------");
        Map<String, Object> reqParams = new HashMap<>();
        
        String response = httpservice.makeHttpGetCall("https://catfact.ninja/fact", reqParams , getHttpHeaders(), String.class);
		return response;
	} 

	/*@GetMapping("/greetingresponse")
	public String greetingresponse() {
		//return new Greeting(counter.incrementAndGet(), String.format(template, name));
		System.out.println("--------------Started 3------------------------");
        Map<String, Object> reqParams = new HashMap<>();
        //reqParams.put("value", "jas");
        
        String response = httpservice.makeHttpGetCall("http://localhost:8080/greeting", reqParams , getHttpHeaders(), String.class);
		//Map<String, Object> reqParams, HttpHeaders headers, Class<T> responseType
		return response;
	} 
	*/
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    
    private String getGreeting() {
        return "Have a nice day ! from get greeting function. ";
    }
}
