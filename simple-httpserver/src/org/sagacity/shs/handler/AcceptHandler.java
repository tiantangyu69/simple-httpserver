/**
 * 
 */
package org.sagacity.shs.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.sagacity.shs.ChannelIO;

/**
 * @author lizhitao
 * @action 处理连接事件
 */
public class AcceptHandler implements Handler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sagacity.shs.handler.Handler#handler(java.nio.channels.SelectionKey)
	 */
	@Override
	public void handler(SelectionKey key) throws IOException {
		// 处理接受连接事件
		ServerSocketChannel channel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = channel.accept();
		System.out.println("接收到客户端连接：地址"
				+ socketChannel.socket().getInetAddress() + ",端口号"
				+ socketChannel.socket().getPort());
		socketChannel.configureBlocking(false);
		
		if (socketChannel == null) {
			return;
		}

		ChannelIO cio = new ChannelIO(socketChannel, false);

		RequestHander hander = new RequestHander(cio);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, hander);
	}

}
