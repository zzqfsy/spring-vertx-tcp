package com.zzqfsy.vertxtcp.vertx;

import com.zzqfsy.vertxtcp.vertx.server.VertxTcpServerVerticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    @Autowired
    private VertxTcpServerVerticle vertxTcpServerVerticle;

    @PostConstruct
    public void deployVerticle() {
        Vertx.vertx().deployVerticle(vertxTcpServerVerticle);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}