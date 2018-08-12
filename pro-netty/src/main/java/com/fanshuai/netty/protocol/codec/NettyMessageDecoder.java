package com.fanshuai.netty.protocol.codec;

import com.fanshuai.netty.protocol.message.Attachment;
import com.fanshuai.netty.protocol.message.Body;
import com.fanshuai.netty.protocol.message.Header;
import com.fanshuai.netty.protocol.message.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;
import java.util.Map;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
                               int lengthFiledLength) {
        //长度字段提取
        super(maxFrameLength, lengthFieldOffset, lengthFiledLength);
    }

    protected Object decode(ChannelHandlerContext context, ByteBuf byteBuf) throws Exception{
        ByteBuf frame = (ByteBuf) super.decode(context, byteBuf);

        if (frame == null) {
            return null;
        }

        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int attachSize = frame.readInt();
        if (attachSize > 0) {
            Map<String, Attachment> map = new HashMap<>(attachSize);
            for (int i = 0; i < attachSize; i++) {
                int keySize = frame.readInt();
                byte[] keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                String key = new String(keyArray, "utf-8");
                Attachment attachment =  MessagePackCodec.getInstance().decode(frame, Attachment.class);
                map.put(key, attachment);
            }

            header.setAttachment(map);
        }
        nettyMessage.setHeader(header);

        if (frame.readableBytes() == 0) {
            nettyMessage.setBody(null);
        } else {

            Body body = MessagePackCodec.getInstance().decode(frame, Body.class);
            nettyMessage.setBody(body);
        }

        return nettyMessage;
    }

}
