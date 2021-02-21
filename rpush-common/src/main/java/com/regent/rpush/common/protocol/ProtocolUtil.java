package com.regent.rpush.common.protocol;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtocolUtil {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        MessageProto.MessageProtocol protocol = MessageProto.MessageProtocol.newBuilder()
                .setSendTo(12)
                .setContent("测试")
                .setFromTo(22)
                .setType(3)
                .build();

        byte[] encode = encode(protocol);

        MessageProto.MessageProtocol parseFrom = decode(encode);

        System.out.println(parseFrom.getContent());
        System.out.println(protocol.toString());
        System.out.println(protocol.toString().equals(parseFrom.toString()));
    }

    /**
     * 编码
     */
    public static byte[] encode(MessageProto.MessageProtocol protocol) {
        return protocol.toByteArray();
    }

    /**
     * 解码
     */
    public static MessageProto.MessageProtocol decode(byte[] bytes) throws InvalidProtocolBufferException {
        return MessageProto.MessageProtocol.parseFrom(bytes);
    }
}