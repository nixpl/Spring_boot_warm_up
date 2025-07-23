package com.example.demo.exception;

import com.example.demo.exception.info.ExceptionInfo;

public class WrongFilterArgumentException extends UniversalException {

  public WrongFilterArgumentException(ExceptionInfo exceptionInfo) {
    super(exceptionInfo);
  }
  public WrongFilterArgumentException(ExceptionInfo exceptionInfo, Object... args) {
    super(exceptionInfo,args);
  }
}
