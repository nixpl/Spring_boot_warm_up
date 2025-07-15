package com.example.demo.exception;

import com.example.demo.exception.info.ExceptionInfo;
import lombok.Getter;

public class UniversalException extends RuntimeException {

    @Getter
    private final ExceptionInfo exceptionInfo;

    public UniversalException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo.getMessage());
        this.exceptionInfo = exceptionInfo;
    }
    public UniversalException(ExceptionInfo exceptionInfo, Object... args) {
        super(String.format(exceptionInfo.getMessage(), args));
        this.exceptionInfo = exceptionInfo;
    }
}
