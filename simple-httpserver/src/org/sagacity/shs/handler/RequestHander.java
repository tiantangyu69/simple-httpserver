/**
 * 
 */
package org.sagacity.shs.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import org.sagacity.shs.ChannelIO;
import org.sagacity.shs.http.FileContent;
import org.sagacity.shs.http.Request;
import org.sagacity.shs.http.Response;
import org.sagacity.shs.http.StringContent;

/**
 * @author lizhitao
 * 
 */
public class RequestHander implements Handler {
	private ChannelIO channelIO;
	private ByteBuffer requestByteBuffer = null;// 存放http请求的缓冲区

	private boolean requestReceived = false; // 表示是否已经接到http请求的所有数据
	private Request request;
	private Response response;

	public RequestHander(ChannelIO channelIO) {
		this.channelIO = channelIO;
	}

	private boolean receive(SelectionKey key) throws IOException {
		if (requestReceived) {
			return true;
		}

		if (channelIO.read() < 0 || Request.isComplete(channelIO.getBuffer())) {
			requestByteBuffer = channelIO.getBuffer();
			return (requestReceived = true);
		}
		return false;
	}

	private boolean parse() throws IOException {
		try {
			request = Request.parse(requestByteBuffer);
			return true;
		} catch (Exception e) {
			response = new Response(Response.Code.BAD_REQUEST,
					new StringContent());
		}
		return false;
	}

	private void build() throws IOException {
		Request.Action action = request.action();

		if (action != Request.Action.GET && action != Request.Action.HEAD) {
			response = new Response(Response.Code.METHOD_NOT_ALLOWED,
					new StringContent());
		} else {
			response = new Response(Response.Code.OK, new FileContent(
					request.uri()), action);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sagacity.shs.handler.Handler#handler(java.nio.channels.SelectionKey)
	 */
	@Override
	public void handler(SelectionKey key) throws IOException {
		try {
			if (null == request) {
				if (!receive(key))
					return;
				requestByteBuffer.flip();

				if (parse()) {
					build();
				}

				try {
					response.prepare();
				} catch (IOException exception) {
					response.release();
					response = new Response(Response.Code.NOT_FOUND,
							new StringContent());
					response.prepare();
				}

				if (send()) {
					key.interestOps(SelectionKey.OP_WRITE);
				} else {
					channelIO.close();
					response.release();
				}
			} else {
				if (!send()) {
					channelIO.close();
					response.release();
				}
			}
		} catch (IOException e) {
			channelIO.close();
			response.release();
		}
	}

	private boolean send() throws IOException {
		return response.send(channelIO);
	}

}
