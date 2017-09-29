//package com.zzqfsy.vertxtcp.vertx.client;
//
//import io.vertx.core.Vertx;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import javax.annotation.PostConstruct;
//
//@SpringBootApplication
//public class ApplicationClient {
//    @Autowired
//    private VertxTcpClientVerticle vertxTcpClientVerticle;
//
//    public static void main(String[] args) {
//        SpringApplication.run(ApplicationClient.class, args);
//    }
//
//    @PostConstruct
//    public void deployVerticle() {
//        Vertx.vertx().deployVerticle(vertxTcpClientVerticle);
//    }
//}
