package com.example;

import javax.ejb.Stateless;

@Stateless
public class MyEJBBean {
    public String getMessage() {
        return "Hello from EJB Bean!";
    }
}