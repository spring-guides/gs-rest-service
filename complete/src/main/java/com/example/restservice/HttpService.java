package com.example.restservice;


import java.util.Map;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpService {

    //private RestTemplate restTemplate;
    
   // @Autowired
   // public HttpService(RestTemplate restTemplate) {
    //    this.restTemplate = new RestTemplate();
    //}
    
    @Autowired
    public HttpService() {
        //this.restTemplate = new RestTemplate();
    }
    
    public <T> T makeHttpGetCall(String url, Map<String, Object> reqParams, HttpHeaders headers, Class<T> responseType)
            throws RestClientException {
        //long httpStartTime = System.currentTimeMillis();
        try {
        	RestTemplate restTemplate = new RestTemplate();
        	
            return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), responseType, reqParams)
                    .getBody();
        } finally {
        	System.out.println("done");
            //log.info("PERF_TIME_TAKEN HTTP_GET | url={} | timeTaken={}", url, (System.currentTimeMillis() - httpStartTime));
        }
    }
}
 