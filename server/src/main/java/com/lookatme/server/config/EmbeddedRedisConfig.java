package com.lookatme.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@RequiredArgsConstructor
@Profile({"default", "local-dev"}) // 해당 프로필에서만 embedded redis 동작
@Configuration
public class EmbeddedRedisConfig {

    private final RedisProperties redisProperties;
    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        redisServer = RedisServer.builder()
                        .port(redisProperties.getPort())
                        .setting("maxmemory 128M")
                        .build();
        try {
            log.info(">>>>>>>>>>>> Redis 실행");
            redisServer.start();
        } catch (Exception e) {
            log.info(">>>>>>>>>>>> 기존 Redis 사용");
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            log.info(">>>>>>>>>>>> Redis 종료");
            redisServer.stop();
        }
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return !StringUtils.hasText(pidInfo.toString());
    }
}
