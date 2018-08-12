package com.fanshuai.netty.codec.messagepack.codec.server;

import com.fanshuai.netty.codec.messagepack.codec.message.EchoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class MsgPackEncoder extends MessageToByteEncoder<EchoMessage> {

    protected void encode(ChannelHandlerContext context, EchoMessage msg, ByteBuf byteBuf) throws Exception{
        MessagePack messagePack = new MessagePack();
        messagePack.register(EchoMessage.class);
        byte[] raw = messagePack.write(msg);

        byteBuf.writeBytes(raw);
    }
}
