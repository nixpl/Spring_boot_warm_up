package com.example.demo.exception;

import com.example.demo.exception.info.ExceptionInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DisposableEmailException extends UniversalException {

    public DisposableEmailException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }
    public DisposableEmailException(ExceptionInfo exceptionInfo, Object... args) {
        super(exceptionInfo,args);
    }
}
