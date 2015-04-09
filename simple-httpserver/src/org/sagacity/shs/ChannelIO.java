/**
 * 
 */
package org.sagacity.shs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author lizhitao
 * 
 */
public class ChannelIO {
	protected SocketChannel socketChannel;
	protected ByteBuffer requestBuffer;
	private static int requestBufferSize = 4096;

	public ChannelIO(SocketChannel socketChannel, boolean blocking) {
		this.socketChannel = socketChannel;
		try {
			socketChannel.configureBlocking(blocking);
		} catch (IOException e) {
			e.printStackTrace();
		}
		requestBuffer = ByteBuffer.allocate(requestBufferSize);
	}

	public SocketChannel getSocketChannel() {
		return this.socketChannel;
	}

	/**
	 * 容量不够时对requestBuffer进行扩容，扩容为原容量的2倍
	 * 
	 * @param remaining
	 */
	protected void resizeRequestBuffer(int remaining) {
		if (requestBuffer.remaining() < remaining) {
			ByteBuffer newBuffer = ByteBuffer
					.allocate(requestBuffer.capacity() * 2);
			requestBuffer.flip();
			newBuffer.put(requestBuffer);
			requestBuffer = newBuffer;
		}
	}

	public int read() throws IOException {
		resizeRequestBuffer(requestBufferSize / 20);
		return socketChannel.read(requestBuffer);
	}

	public ByteBuffer getBuffer() {
		return this.requestBuffer;
	}

	public int write() throws IOException {
		return socketChannel.write(requestBuffer);
	}

	public long transferTo(FileChannel channel, long pos, long len)
			throws IOException {
		return channel.transferTo(pos, len, socketChannel);
	}

	public void close() throws IOException {
		socketChannel.close();
	}
}
