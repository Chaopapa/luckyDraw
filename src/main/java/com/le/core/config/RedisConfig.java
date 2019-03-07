package com.le.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName DateConverterConfig
 * @Author lz
 * @Description Redis 配置类
 * @Date 2018/10/8 10:12
 * @Version V1.0
 **/
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger Logger = LoggerFactory.getLogger(RedisConfig.class);

    private StringRedisSerializer keySerializer(){
        Charset charset = Charset.forName("UTF-8");
        return new StringRedisSerializer(charset);
    }

    private RedisSerializer valueSerializer(){
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        serializer.setObjectMapper(om);
        return serializer;
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer keySerializer = keySerializer();
        RedisSerializer valueSerializer = valueSerializer();
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        Logger.info("初始化 -> [{}]", "Redis CacheErrorHandler");
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                Logger.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                Logger.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                Logger.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                Logger.error("Redis occur handleCacheClearError：", e);
            }
        };
        return cacheErrorHandler;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializationContext.SerializationPair<String> keySerializer = RedisSerializationContext.SerializationPair.fromSerializer(keySerializer());
        RedisSerializationContext.SerializationPair<Object> valueSerializer = RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer());
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig(); // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        config = config.serializeKeysWith(keySerializer)
                .serializeValuesWith(valueSerializer)
                //.entryTtl(Duration.ofMinutes(60)) // 设置缓存的默认过期时间，也是使用Duration设置
                .disableCachingNullValues(); // 不缓存空值

        Set<String> cacheNames =  new HashSet<>();
        cacheNames.add("token");

        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("token", config.entryTtl(Duration.ofHours(6)));

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(config)
                .initialCacheNames(cacheNames).withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }

    @ConfigurationProperties
    class DataJedisProperties {

        @Value("${spring.redis.host}")
        public String host;
        @Value("${spring.redis.port}")
        public int port;
        @Value("${spring.redis.password}")
        public String password;
        @Value("${spring.redis.database}")
        public int database;
        @Value("${spring.redis.jedis.pool.max-idle}")
        public int maxIdle;
        @Value("${spring.redis.jedis.pool.min-idle}")
        public int minIdle;
        @Value("${spring.redis.jedis.pool.max-active}")
        public int maxActive;
        @Value("${spring.redis.jedis.pool.max-wait}")
        public String maxWait;

        @Bean
        public JedisConnectionFactory jedisConnectionFactory() {
            Logger.info("Create JedisConnectionFactory successful");
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(host);
            redisConfig.setPort(port);
            redisConfig.setPassword(RedisPassword.of(password));
            JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMinIdle(minIdle);
            jedisPoolConfig.setMaxTotal(maxActive);
            int maxWaitMillis = Integer.parseInt(maxWait.substring(0, maxWait.length() - 2));
            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
            jpb.poolConfig(jedisPoolConfig);
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfig, jpb.build());
            return jedisConnectionFactory;
        }
    }

}