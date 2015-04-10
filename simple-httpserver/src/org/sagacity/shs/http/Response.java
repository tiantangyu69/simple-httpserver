/**
 * 
 */
package org.sagacity.shs.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.sagacity.shs.ChannelIO;

/**
 * @author lizhitao
 * 
 */
public class Response implements Sendable {
	public static class Code {
		private int number;
		private String reason;

		private Code(int number, String reason) {
			this.number = number;
			this.reason = reason;
		}

		public String toString() {
			return number + " " + reason;
		}

		public static Code OK = new Code(200, "OK");
		public static Code BAD_REQUEST = new Code(400, "Bad Request");
		public static Code NOT_FOUND = new Code(404, "Not Found");
		public static Code METHOD_NOT_ALLOWED = new Code(405, "Method Not Allowed");
	}

	private Code code;// 状态代码
	private Content content;// 响应正文
	private boolean headersOnly;// 是否只包含响应头
	private ByteBuffer headBuffer = null;// 响应头

	public Response(Code code, Content content) {
		this(code, content, null);
	}

	public Response(Code code, Content content, Request.Action head) {
		this.code = code;
		this.content = content;
		headersOnly = (head == Request.Action.HEAD);
	}

	private static String CLRF = "\r\n";
	private static Charset responseCharset = Charset.forName("utf-8");

	private ByteBuffer headers() {
		CharBuffer cb = CharBuffer.allocate(1024);
		while (true) {
			try {
				cb.put("HTTP/1.1 ").put(code.toString()).put(CLRF);
				cb.put("Server:nio/1.1").put(CLRF);
				cb.put("Content-type:").put(content.type()).put(CLRF);
				cb.put("Content-length:").put(Long.toString(content.length()))
						.put(CLRF);
				cb.put(CLRF);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				assert (cb.capacity() < (1 << 16));
				cb = CharBuffer.allocate(cb.capacity() * 2);
				continue;
			}
		}
		cb.flip();
		return responseCharset.encode(cb);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sagacity.shs.http.Sendable#prepare()
	 */
	@Override
	public void prepare() throws IOException {
		content.prepare();
		headBuffer = headers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sagacity.shs.http.Sendable#send(org.sagacity.shs.ChannelIO)
	 */
	@Override
	public boolean send(ChannelIO io) throws IOException {
		if (headBuffer == null) {
			throw new RuntimeException();
		}

		if (headBuffer.hasRemaining()) {
			if (io.write(headBuffer) <= 0) {
				return true;
			}
		}

		if (!headersOnly) {
			if (content.send(io)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sagacity.shs.http.Sendable#release()
	 */
	@Override
	public void release() throws IOException {
		content.release();
	}

}
