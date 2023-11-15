package it.pagopa.interop.signalhub.updater.config;

import it.pagopa.interop.signalhub.updater.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.updater.cache.model.OrganizationEServiceCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisCacheConfig {

    @Bean
    @Qualifier("RedisTemplateConsumer")
    RedisTemplate<String, ConsumerEServiceCache> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,ConsumerEServiceCache> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        Jackson2JsonRedisSerializer<ConsumerEServiceCache> valueSerializer =
                new Jackson2JsonRedisSerializer<>(ConsumerEServiceCache.class);
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }

    @Bean
    @Qualifier("RedisTemplateOrganization")
    RedisTemplate<String, OrganizationEServiceCache> redisTemplateOrganization(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, OrganizationEServiceCache> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        Jackson2JsonRedisSerializer<OrganizationEServiceCache> valueSerializer =
                new Jackson2JsonRedisSerializer<>(OrganizationEServiceCache.class);
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }
}