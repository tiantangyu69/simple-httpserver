/**
 * 
 */
package org.sagacity.shs.http;

import java.io.IOException;

import org.sagacity.shs.ChannelIO;

/**
 * @author lizhitao
 * 
 */
public interface Sendable {
	/**
	 * 准备发送的内容
	 * 
	 * @throws IOException
	 */
	void prepare() throws IOException;

	/**
	 * 利用通道发送部分内容，如果所有内容发送完毕就返回false，如果还有内容可发送返回true，
	 * 如果内容还没有准备好就抛出illegalStateException
	 * 
	 * @param io
	 * @return
	 * @throws IOException
	 */
	boolean send(ChannelIO io) throws IOException;

	/**
	 * 当服务器发送内容完毕，就调用此方法，释放内容占用的资源
	 * @throws IOException
	 */
	void release() throws IOException;
}
