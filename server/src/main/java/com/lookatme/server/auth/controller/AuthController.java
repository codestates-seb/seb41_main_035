package com.lookatme.server.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile() { // 현재 실행중인 real 프로필 조회 (8081인지 8082인지 확인하기 위함)
        List<String> profiles = Arrays.asList(env.getActiveProfiles()); // 현재 실행중인 ActiveProfile 조회 (dev, real1/2)
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "dev" : profiles.get(0);

        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
