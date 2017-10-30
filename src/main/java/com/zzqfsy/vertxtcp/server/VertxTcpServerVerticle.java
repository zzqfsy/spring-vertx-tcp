package com.zzqfsy.vertxtcp.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zzqfsy.vertxtcp.domain.PersonDomain;
import com.zzqfsy.vertxtcp.protocol.FrameParser;
import io.vertx.core.AbstractVerticle;
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

        socket.handler(new FrameParser(res -> {
            if (res.failed()) {
                // could not parse the message properly
                System.out.println(res.cause());
                return;
            }
            Object object = res.result();
            byte[] bytes = (byte[])object;
            try {
                PersonDomain.Person person = PersonDomain.Person.parseFrom(bytes);
                System.out.println(person);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            System.out.println(res.result());
        }));

        socket.exceptionHandler(t -> {
            System.out.println(t.getMessage());
            socket.close();
        });

        socket.endHandler(v -> {
        });
    }
}

