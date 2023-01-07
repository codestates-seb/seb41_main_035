package com.lookatme.server.auth.utils;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponder {

    public static void sendErrorResponse(HttpServletResponse response, HttpStatus status) throws IOException {
        Gson gson = new Gson();
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setStatus(status.value());
        response.getWriter().write("Error Happened :P");
    }
}
