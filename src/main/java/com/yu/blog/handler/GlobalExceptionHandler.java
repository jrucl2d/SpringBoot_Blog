package com.yu.blog.handler;

import com.yu.blog.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice // 어디에서든 exception 발생하면 이쪽으로 오게 하기
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value=Exception.class) // 해당 exception을 여기서 처리
    public ResponseDto<String> handleArgumentException(Exception e){
        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
