package io.infra.structure.redis.autoconfiguration;

import lombok.extern.slf4j.Slf4j;
import io.infra.structure.core.tool.BinderTool;
import io.infra.structure.redis.constants.Const;
import io.infra.structure.redis.definition.RedisBeanDefinitionRegistry;
import io.infra.structure.redis.properties.RedisConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author sven
 * Created on 2022/1/14 10:25 下午
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = Const.configPrefix, value = "enabled", havingValue = "true")
public class InfraRedisAutoConfiguration {
    @Bean
    public static RedisConfig redisConfig(Environment env) {
        return BinderTool.bind(env, Const.configPrefix, RedisConfig.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public static RedisBeanDefinitionRegistry redisDefinitionRegistry(RedisConfig redisConfig) {
        return new RedisBeanDefinitionRegistry(redisConfig);
    }
}
