package com.example;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyCdiBean {
    public String getMessage() {
        return "Hello from CDI Bean!";
    }
}