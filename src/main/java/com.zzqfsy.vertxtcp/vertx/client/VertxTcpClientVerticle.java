package com.zzqfsy.vertxtcp.vertx.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.springframework.stereotype.Component;

//@Component
public class VertxTcpClientVerticle extends AbstractVerticle {

    @Override
    public void start() {
        NetClient tcpClient = Vertx.vertx().createNetClient();

        tcpClient.connect(8380, "127.0.0.1", this::handle);
    }

    public void handle(AsyncResult<NetSocket> result) {
        NetSocket socket = result.result();

        //read data
        socket.handler(new Handler<Buffer>(){
            @Override
            public void handle(Buffer buffer) {
                System.out.println("Received data length: " + buffer.length());

                String data = buffer.getString(0, buffer.length());
                System.out.println("Received data: " + data);

            }
        });

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< 10000; i++){
            sb.append(i);
        }
        //write data
        String input = "GET / HTTP/1.1\\r\\nHost: jenkov.com\\r\\n\\r\\n";
        System.out.println("write: " + input);
        socket.write(input);

        System.out.println("write: " + sb);
        socket.write(sb.toString());



        socket.exceptionHandler(t -> {
            System.out.println(t.getMessage());
            socket.close();
        });

        socket.endHandler(v -> {
        });
    }
}
