package com.fanshuai.netty.protocol.codec;

import com.fanshuai.netty.protocol.message.Attachment;
import com.fanshuai.netty.protocol.message.Body;
import com.fanshuai.netty.protocol.message.Header;
import com.fanshuai.netty.protocol.message.NettyMessage;
import io.netty.buffer.ByteBuf;
import org.msgpack.MessagePack;

public class MessagePackCodec {
    private MessagePack messagePack;

    private static MessagePackCodec instance = null;

    public static MessagePackCodec getInstance() {
        if (instance == null) {
            synchronized (MessagePackCodec.class) {
                if (instance == null) {
                    instance = new MessagePackCodec();
                }
            }
        }

        return instance;
    }

    private MessagePackCodec() {
        messagePack = new MessagePack();
        messagePack.register(NettyMessage.class);
        messagePack.register(Header.class);
        messagePack.register(Attachment.class);
        messagePack.register(Body.class);
    }

    public <T> void encode(T msg, ByteBuf byteBuf) throws Exception{
        byte[] data = messagePack.write(msg);
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }

    public <T> T decode(ByteBuf byteBuf, Class<T> cls) throws Exception {
        int length = byteBuf.readInt();
        byte[] data = new byte[length];
        byteBuf.readBytes(data, 0, length);

        return messagePack.read(data, cls);
    }
}
