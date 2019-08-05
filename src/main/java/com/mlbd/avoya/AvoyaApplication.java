package com.mlbd.avoya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mlbd"})
public class AvoyaApplication {

  public static void main(String[] args) {
    
    SpringApplication.run(AvoyaApplication.class, args);
  }
}
