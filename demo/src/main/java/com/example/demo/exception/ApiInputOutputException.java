package com.example.demo.exception;

import com.example.demo.exception.info.ExceptionInfo;

public class ApiInputOutputException extends UniversalException {
    
    public ApiInputOutputException(ExceptionInfo exceptionInfo) {super(exceptionInfo);}
    public ApiInputOutputException(ExceptionInfo exceptionInfo, Object... args) {
        super(exceptionInfo,args);
    }
}
