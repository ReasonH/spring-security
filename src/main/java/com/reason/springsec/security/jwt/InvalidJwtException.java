package com.reason.springsec.security.jwt;

public class InvalidJwtException extends Exception {
    public InvalidJwtException(String msg){
        super(msg);
    }
}
