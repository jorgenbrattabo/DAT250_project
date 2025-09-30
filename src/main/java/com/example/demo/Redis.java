package com.example.demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.UnifiedJedis;

@Configuration
public class Redis {
    @Bean
    public UnifiedJedis jedis() {
        return new UnifiedJedis("redis://localhost:6379");
    }
}