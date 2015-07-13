/**
 * 
 */
package org.sagacity.shs.http;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;

import org.sagacity.shs.ChannelIO;

/**
 * @author lizhitao
 * 
 */
public class FileContent implements Content {
	private static File ROOT = new File("root");
	private File file;

	public FileContent(URI uri) {
		file = new File(ROOT, uri.getPath().replace('/', File.separatorChar));
	}

	private String type = null;

	@Override
	public String type() {
		if (type != null) {
			return type;
		}

		String nm = file.getName();

		if (nm.endsWith(".html") || nm.endsWith(".htm")) {
			type = "text/html;charset=utf-8";
		} else if ((nm.indexOf(".") < 0) || nm.endsWith(".txt")) {
			type = "text/plain;charset=utf-8";
		} else {
			type = "application/octet-stream";
		}
		return type;
	}

	private FileChannel fileChannel = null;
	private long length = -1;
	private long position = -1;

	@Override
	public void prepare() throws IOException {
		if (fileChannel == null) {
			fileChannel = new RandomAccessFile(file, "r").getChannel();
		}
		length = fileChannel.size();
		position = 0;
	}

	@Override
	public boolean send(ChannelIO io) throws IOException {
		// TODO Auto-generated method stub
		if (fileChannel == null) {
			throw new IllegalStateException();
		}

		if (position < 0) {
			throw new IllegalStateException();
		}

		if (position >= length) {
			return false;
		}

		position += io.transferTo(fileChannel, position, length - position);
		return (position < length);
	}

	@Override
	public void release() throws IOException {
		// TODO Auto-generated method stub
		if (null != fileChannel) {
			fileChannel.close();
			fileChannel = null;
		}
	}

	@Override
	public long length() {
		return length;
	}

}
