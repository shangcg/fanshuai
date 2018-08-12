package com.fanshuai.netty.protocol.codec;

import com.fanshuai.netty.protocol.message.Attachment;
import com.fanshuai.netty.protocol.message.Header;
import com.fanshuai.netty.protocol.message.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    protected void encode(ChannelHandlerContext context, NettyMessage message, List<Object> list) throws Exception{
        if (message == null || message.getHeader() == null) {
            throw new Exception("the message is null");
        }

        ByteBuf buf = Unpooled.buffer();
        Header header = message.getHeader();

        buf.writeInt(header.getCrcCode());
        buf.writeInt(header.getLength());
        buf.writeLong(header.getSessionId());
        buf.writeByte(header.getType());
        buf.writeByte(header.getPriority());

        buf.writeInt(header.getAttachment().size());
        String key = null;
        byte[] keyArray = null;

        if (header.getAttachment().size() > 0) {
            for (Map.Entry<String, Attachment> entry : header.getAttachment().entrySet()) {
                key = entry.getKey();
                keyArray = key.getBytes("utf-8");

                buf.writeInt(keyArray.length);
                buf.writeBytes(keyArray);
                MessagePackCodec.getInstance().encode(entry.getValue(), buf);
            }
        }

        if (message.getBody() != null) {
            MessagePackCodec.getInstance().encode(message.getBody(), buf);
        } else {
            buf.writeInt(0);
        }

        buf.setInt(4, buf.readableBytes());
    }
}
