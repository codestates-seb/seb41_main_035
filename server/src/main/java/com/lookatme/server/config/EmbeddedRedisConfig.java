package com.lookatme.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {

        redisPort = isRedisRunning() ? findAvailablePort() : redisPort;

        redisServer = RedisServer.builder()
                        .port(redisPort)
                        .setting("maxmemory 128M")
                        .build();

        log.info(">>>>>>>>>>>> {} 포트에서 Redis 실행", redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            log.info(">>>>>>>>>>>> {} 포트에서 Redis 종료", redisPort);
            redisServer.stop();
        }
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    public int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }
        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    private Process executeGrepProcessCommand(int port) throws IOException {
        Process process = null;
        try {
            String command = String.format("netstat -nat | grep LISTEN | grep %d", port); // Linux
            String[] shell = {"/bin/sh", "-c", command};
            process = Runtime.getRuntime().exec(shell);
        } catch (IOException e) {
            String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port); // Windows
            String[] shell = {"cmd.exe", "/y", "/c", command};
            process = Runtime.getRuntime().exec(shell);
        }
        return process;
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
        } catch (Exception e) {}
        return StringUtils.hasText(pidInfo.toString());
    }
}
