package ru.urfu.pizzaSite.RestApiPizzaApplication.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class LongGenerator {
    private final SecureRandom secureRandom = new SecureRandom();
    
    public long generateLong(){
       return secureRandom.nextLong(0,Long.MAX_VALUE);
    }
}
