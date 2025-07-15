package com.example.demo.exception;

import com.example.demo.exception.info.ExceptionInfo;

public class EntityNotFoundException extends UniversalException {
    public EntityNotFoundException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }
    public EntityNotFoundException(ExceptionInfo exceptionInfo, Object... args) {
        super(exceptionInfo,args);
    }
}
