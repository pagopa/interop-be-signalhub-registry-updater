package it.pagopa.interop.signalhub.updater.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisCacheConfig {


    @Bean
    public JedisConnectionFactory connectionFactory() {
        log.info("Redis configuration: [ hostname = " + "localHost" + " - port = " + "6379" + "]");

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        String redisHostname = "localhost";
        int redisPort = 6379;
        configuration.setHostName(redisHostname);
        configuration.setPort(redisPort);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    @Primary
    RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }
}