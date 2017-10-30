package com.zzqfsy.vertxtcp.test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zzqfsy.vertxtcp.domain.PersonDomain;
import org.junit.Test;

public class ProtobufTest {

    @Test
    public void test() throws InvalidProtocolBufferException {
        //模拟将对象转成byte[]，方便传输
        PersonDomain.Person.Builder builder = PersonDomain.Person.newBuilder();
        builder.setId(1);
        builder.setName("zzqfsy");
        builder.setEmail("zzqfsy@gmail.com");
        PersonDomain.Person person = builder.build();
        System.out.println("before :"+ person.toString());

        System.out.println("===========Person Byte==========");
        for(byte b : person.toByteArray()){
            System.out.print(b);
        }
        System.out.println();
        System.out.println(person.toByteString());
        System.out.println("================================");

        //模拟接收Byte[]，反序列化成Person类
        byte[] byteArray =person.toByteArray();
        PersonDomain.Person p2 = PersonDomain.Person.parseFrom(byteArray);
        System.out.println("after :" +p2.toString());
    }
}
