package com.lookatme.server.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @MockBean
    private AmazonS3Client amazonS3Client;

    @DisplayName("파일 타입 체크 테스트")
    @ValueSource(strings = {"image/png", "image/jpeg"})
    @ParameterizedTest
    void imageTypeCheckTest(String fileType) throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "image", "fileName", fileType, "<<data>>".getBytes()
        );

        // When
        Throwable exception = catchThrowable(() -> fileService.upload(file, "dirName"));

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.FILE_CONVERT_FAILED.getValue());
    }

    @DisplayName("파일 타입 체크 실패 테스트 - png, jpg 제외 나머지 전부")
    @ValueSource(strings = {"text/plain", "application/*", "image/gif"})
    @ParameterizedTest
    void imageTypeCheckFailTest(String fileType) throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "image", "fileName", fileType, "<<data>>".getBytes()
        );

        // When
        Throwable exception = catchThrowable(() -> fileService.upload(file, "dirName"));

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.FILE_TYPE_NOT_SUPPORTED.getValue());
    }
}