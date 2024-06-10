package com.haoren.springbootshirovuebeta.config.system;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.haoren.springbootshirovuebeta.dto.SessionUserInfo;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean(name = "tokenCacheManager")
    public Cache<String, SessionUserInfo> caffeineCache(){
        return Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .initialCapacity(1000)
                .maximumSize(1000)
                .build();
    }
}
