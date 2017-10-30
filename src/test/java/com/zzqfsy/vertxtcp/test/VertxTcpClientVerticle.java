package com.zzqfsy.vertxtcp.test;

import com.zzqfsy.vertxtcp.vertx.domain.Person;
import com.zzqfsy.vertxtcp.vertx.protocol.FrameHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

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

        Person person = new Person();
        person.setColor("yellow");
        person.setSex("male");
        JsonObject jsonObject = JsonObject.mapFrom(person);
        for (int i = 0; i < 500; i++){
            jsonObject.put("" + i, i);
        }
        FrameHelper.writeFrame(jsonObject, socket);

        socket.exceptionHandler(t -> {
            System.out.println(t.getMessage());
            socket.close();
        });

        socket.endHandler(v -> {
        });
    }

    public static void main(String[] args) {
        VertxTcpClientVerticle client = new VertxTcpClientVerticle();
        Vertx.vertx().deployVerticle(client);
    }
}
