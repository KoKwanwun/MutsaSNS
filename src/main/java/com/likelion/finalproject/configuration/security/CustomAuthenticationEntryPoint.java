package com.likelion.finalproject.configuration.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.ErrorResponse;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Token이 없거나 Bearer로 시작하지 않는 경우
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            setResponse(ErrorCode.INVALID_TOKEN, response);
            return;
        }

        // 해당 예외가 없는데 에러가 난 경우
        String exception = (String)request.getAttribute("exception");

        if(exception == null){
            setResponse(ErrorCode.INVALID_PERMISSION, response);
        }
    }

    private void setResponse(ErrorCode errorCode, HttpServletResponse res) throws IOException {
        res.setStatus(errorCode.getStatus().value());
        res.setContentType("application/json;charset=UTF-8");

        Response errorResponse = Response.error(new ErrorResponse(errorCode.name(), errorCode.getMessage()));
        res.getWriter().write(convertToJson(errorResponse));
    }

    private String convertToJson(Response errorResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(errorResponse);
        return jsonStr;
    }
}
