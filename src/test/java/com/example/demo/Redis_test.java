package com.example.demo;

//Used this to test out Redis.

import redis.clients.jedis.UnifiedJedis;

public class Redis_test {
    public static void main(String[] args) {
        UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

        // Code that interacts with Redis...

        String res1 = jedis.set("Car:1", "Tesla");
        System.out.println(res1); // OK

        String res2 = jedis.get("Car:1");
        System.out.println(res2); // Tesla

        jedis.close();
    }
}

