package io.github.hizhangbo.netty.tcp.exp2;

/**
 * @author Bob
 * @date 2020-01-09 12:31
 * 定义协议包传输大小
 */
public class MessageProtocol {
    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
