package com.example.demo.exception;

import com.example.demo.exception.info.ExceptionInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownFilterParameterException extends UniversalException {

    public UnknownFilterParameterException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }

    public UnknownFilterParameterException(ExceptionInfo exceptionInfo, Object... args) {
        super(exceptionInfo,args);
    }
}
