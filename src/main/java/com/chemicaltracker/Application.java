package com.chemicaltracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import org.apache.log4j.Logger;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static final Logger logger = Logger.getLogger(Application.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e) {
            throw new Exception("Error occured while trying to run application", e);
        }
    }
}
