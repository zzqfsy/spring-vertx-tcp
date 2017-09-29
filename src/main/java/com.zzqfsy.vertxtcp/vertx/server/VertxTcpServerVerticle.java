package com.zzqfsy.vertxtcp.vertx.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import org.springframework.stereotype.Component;

@Component
public class VertxTcpServerVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        NetServerOptions options = new NetServerOptions().setPort(8380);


        NetServer server = vertx.createNetServer(options);
        server.connectHandler(this::handle);
        server.close(result -> {
            if(result.succeeded()){
                //TCP server fully closed
                System.out.println("server close succeeded.");
            }else {
                System.out.println("server status: " + result.result().toString());
            }
        });
        server.listen();
    }

    //handle stream
    public void handle(NetSocket socket) {
        System.out.println("Incoming connection!");

        socket.handler(new Handler<Buffer>() {

            @Override
            public void handle(Buffer buffer) {
                if (buffer == null) return;
                System.out.println("incoming data length: "+buffer.length());

                //read buffer
                String data = buffer.getString(0, buffer.length());
                System.out.println("incoming data: "+data);

                //stick package

                //send eventbug to deal business logic

                //write buffer
                Buffer outBuffer = Buffer.buffer();
                outBuffer.appendString("response...");
                System.out.println("response data: "+ outBuffer.toString());

                socket.write(outBuffer);
            }
        });

        socket.exceptionHandler(t -> {
            System.out.println(t.getMessage());
            socket.close();
        });

        socket.endHandler(v -> {
        });
    }
}

