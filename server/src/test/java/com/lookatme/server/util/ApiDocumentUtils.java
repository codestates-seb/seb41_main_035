package com.lookatme.server.util;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface ApiDocumentUtils {

    static OperationRequestPreprocessor getRequestPreprocessor() {
        return preprocessRequest(prettyPrint(), Preprocessors.modifyUris().host("13.125.30.88").removePort());
    }

    static OperationResponsePreprocessor getResponsePreprocessor() {
        return preprocessResponse(prettyPrint());
    }
}
