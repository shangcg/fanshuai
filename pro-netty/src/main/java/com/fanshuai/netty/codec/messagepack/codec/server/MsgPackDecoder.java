package com.fanshuai.netty.codec.messagepack.codec.server;

import com.fanshuai.netty.codec.messagepack.codec.message.EchoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    public void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception{
        int length = byteBuf.readableBytes();
        byte[] data = new byte[length];

        byteBuf.readBytes(data);
        MessagePack messagePack = new MessagePack();
        messagePack.register(EchoMessage.class);

        list.add(messagePack.read(data, EchoMessage.class));
    }
}
