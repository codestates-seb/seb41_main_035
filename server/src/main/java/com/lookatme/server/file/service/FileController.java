package com.lookatme.server.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/file")
@RestController
public class FileController { // *** 테스트용 ***

    private final FileService fileService;

    @PostMapping
    public String test(MultipartFile file) throws IOException {
        return fileService.upload(file, "profile");
    }
}
