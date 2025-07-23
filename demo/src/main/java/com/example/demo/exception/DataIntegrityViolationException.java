package com.example.demo.exception;

import com.example.demo.exception.info.ExceptionInfo;

public class DataIntegrityViolationException extends UniversalException {

    public DataIntegrityViolationException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }
    public DataIntegrityViolationException(ExceptionInfo exceptionInfo, Object... args) {
        super(exceptionInfo,args);
    }
}
