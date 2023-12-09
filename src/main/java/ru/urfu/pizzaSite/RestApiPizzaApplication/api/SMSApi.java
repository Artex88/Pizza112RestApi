package ru.urfu.pizzaSite.RestApiPizzaApplication.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Component
public class SMSApi {
    @Value("${sms_login}")
    private String login;

    @Value("${sms_password}")
    private String password;

    String response;
    public void sendSMSWithPassword(String phoneNumber, String code){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://smsc.ru/rest/send/";
        Map<String, String> jsonToSend;
        jsonToSend = new HashMap<>();
        jsonToSend.put("login", login);
        jsonToSend.put("psw", password);
        jsonToSend.put("phones", phoneNumber);
        jsonToSend.put("mes",code);
        jsonToSend.put("fmt", "0");
        HttpEntity<Map<String,String>> request = new HttpEntity<>(jsonToSend);
        response = restTemplate.postForObject(url, request, String.class);
    }
}
