package it.pagopa.interop.signalhub.updater.config;

import it.pagopa.interop.signalhub.updater.repository.OrganizationEserviceRepository;
import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.updater.repository.cache.model.OrganizationEServiceCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("RedisTemplateConsumer")
    RedisTemplate<String, ConsumerEServiceCache> redisTemplate(){
        RedisTemplate<String,ConsumerEServiceCache> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }

    @Bean
    @Qualifier("RedisTemplateOrganization")
    RedisTemplate<String, OrganizationEServiceCache> redisTemplateOrganization(){
        RedisTemplate<String, OrganizationEServiceCache> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }
}