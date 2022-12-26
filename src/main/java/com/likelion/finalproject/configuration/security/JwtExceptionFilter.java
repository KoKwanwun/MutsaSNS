package com.likelion.finalproject.configuration.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.ErrorResponse;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 토큰의 유효기간이 만료되었을 경우
            setResponse(ErrorCode.INVALID_TOKEN, response);
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않을 경우
            setResponse(ErrorCode.INVALID_TOKEN, response);
        } catch (NoSuchElementException e){
            // 토큰 정보에 있는 유저가 DB에 없을 경우
            setResponse(ErrorCode.DATABASE_ERROR, response);
        }
        // 그 외의 경우는 CustomAuthenticationEntryPoint Class에서 처리
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