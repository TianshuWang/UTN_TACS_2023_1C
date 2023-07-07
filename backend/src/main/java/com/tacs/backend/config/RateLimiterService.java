package com.tacs.backend.config;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {
    private static final Integer DEFAULT_API_RPM = 10;

    private static final Integer DEFAULT_USER_RPM = 10;


    private static final ConcurrentHashMap<String, UserRequest> USERS_REQUESTS = new ConcurrentHashMap<>();

    private static Integer apiRPMRequestCount=0;

    private static long requestApiInitialTime;

    public RateLimiterService() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
                new BasicThreadFactory.Builder().namingPattern("Rate-limiter-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(RateLimiterService::initializeApiRequestLimit,0,5, TimeUnit.SECONDS);
    }

    private static void initializeApiRequestLimit() {
        requestApiInitialTime = System.currentTimeMillis();
        apiRPMRequestCount = 0;
    }

    public void initializeUserRequest(String token) {
        USERS_REQUESTS.computeIfAbsent(token, t -> new UserRequest(System.currentTimeMillis()));
    }

    public boolean reachedMaxRequestAllowed(String token)  {
        apiRPMRequestCount++;
        return apiRPMRequestCount > DEFAULT_API_RPM || reachedMaxUserRequestAllowed(token);
    }

    private boolean reachedMaxUserRequestAllowed(String token) {
        UserRequest userRequest = USERS_REQUESTS.get(token);
        userRequest.incrementCounter();
        return userRequest.getRequestCount() > DEFAULT_USER_RPM;
    }

    @Getter
    @Setter
    static class UserRequest {
        private long lastRequestStartTime;
        private Integer requestCount;
        public UserRequest(long theLastRequestStartTime) {
            lastRequestStartTime = theLastRequestStartTime;
            requestCount = 0;
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
                    new BasicThreadFactory.Builder().namingPattern("Rate-limiter-pool-%d").daemon(true).build());
            executorService.scheduleAtFixedRate(() -> {
                long time = System.currentTimeMillis();
                updateLastRequestStartTime(time);
            },0,5, TimeUnit.SECONDS);
        }
        public void incrementCounter() {
            this.requestCount++;
        }
        public void updateLastRequestStartTime(long time) {
            this.lastRequestStartTime = time;
            this.requestCount = 0;
        }
    }
}