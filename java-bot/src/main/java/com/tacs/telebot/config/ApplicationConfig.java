package com.tacs.telebot.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tacs.telebot.service.ApiFactory;
import com.tacs.telebot.service.ApiManager;
import com.tacs.telebot.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author tianshuwang
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {

    @Value("${backend.baseUrl}")
    private String baseUrl;

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    ApiManager apiManager(Retrofit retrofit) {
        return retrofit.create(ApiManager.class);
    }

    @Bean
    public ObjectWriter objectWriter() {
        return new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ApiFactory apiFactory(ApiService apiService) {
        return new ApiFactory(apiService);
    }
}
