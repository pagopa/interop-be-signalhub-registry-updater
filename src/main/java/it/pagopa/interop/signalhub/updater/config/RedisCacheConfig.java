package it.pagopa.interop.signalhub.updater.config;

import it.pagopa.interop.signalhub.updater.repository.cache.model.EServiceCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@Slf4j
public class RedisCacheConfig {


    @Bean
    public ReactiveRedisTemplate<String, EServiceCache> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveRedisTemplate<>(
                factory,
                RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(EServiceCache.class))
        );
    }
}