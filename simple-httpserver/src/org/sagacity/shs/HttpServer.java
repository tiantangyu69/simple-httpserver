/**
 * 
 */
package org.sagacity.shs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.sagacity.shs.handler.AcceptHandler;
import org.sagacity.shs.handler.Handler;

/**
 * @author lizhitao
 * 
 */
public class HttpServer {
	private int port = 80;
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;

	public HttpServer() throws IOException {
		selector = Selector.open();

		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.configureBlocking(false);// 创建非阻塞ServerSocketChannel
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器启动成功,端口号为：" + port);
	}

	public void service() throws IOException {
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,
				new AcceptHandler());

		while (true) {
			int n = selector.select();
			if (n == 0) {// 如果没有需要处理的事件则继续等待
				continue;
			}

			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = null;
				try {
					key = it.next();
					it.remove();
					Handler handler = (Handler) key.attachment();
					handler.handler(key);
				} catch (Exception e) {
					e.printStackTrace();
					if (null != key) {
						key.cancel();
						key.channel().close();
					}
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new HttpServer().service();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
