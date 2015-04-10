/**
 * 
 */
package org.sagacity.shs.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lizhitao
 * 
 */
public class Request {
	public static class Action {
		private String name;

		private Action(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}

		public static Action GET = new Action("GET");
		public static Action POST = new Action("POST");
		public static Action HEAD = new Action("HEAD");

		public static Action parse(String s) {
			if (s.equals("GET")) {
				return GET;
			}
			if (s.equals("POST")) {
				return POST;
			}

			throw new IllegalArgumentException();
		}
	}

	private Action action;
	private String version;
	private URI uri;

	public Action action() {
		return this.action;
	}

	public String version() {
		return this.version;
	}

	public URI uri() {
		return this.uri;
	}

	private Request(Action action, String version, URI uri) {
		this.action = action;
		this.version = version;
		this.uri = uri;
	}

	public String toString() {
		return action + " " + version + " " + uri;
	}

	private static Charset requestCharset = Charset.forName("utf-8");

	public static boolean isComplete(ByteBuffer bb) {
		ByteBuffer temp = bb.asReadOnlyBuffer();
		temp.flip();
		String data = requestCharset.decode(temp).toString();
		if (data.indexOf("\r\n\r\n") != -1) {
			return true;
		}
		return false;
	}

	private static ByteBuffer deleteContent(ByteBuffer bb) {
		ByteBuffer temp = bb.asReadOnlyBuffer();
		String data = requestCharset.decode(temp).toString();
		if (data.indexOf("\r\n\r\n") != -1) {
			data = data.substring(0, data.indexOf("\r\n\r\n") + 4);
			return requestCharset.encode(data);
		}
		return bb;
	}

	private static Pattern requestPattern = Pattern.compile(
			"\\A([A-Z]+)+([^]+)+HTTP/([0-9\\.]+)$"
					+ ".*^Host:([^]+)$.*\r\n\r\n\\z", Pattern.MULTILINE
					| Pattern.DOTALL);

	public static Request parse(ByteBuffer bb) {
		bb = deleteContent(bb);
		CharBuffer cb = requestCharset.decode(bb);

		Matcher m = requestPattern.matcher(cb);

		if (!m.matches()) {
			throw new RuntimeException();
		}

		Action action;
		action = Action.parse(m.group(1));

		URI uri = null;
		try {
			uri = new URI("http://" + m.group(4) + m.group(2));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return new Request(action, m.group(3), uri);
	}
}
