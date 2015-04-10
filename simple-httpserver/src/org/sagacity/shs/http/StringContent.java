/**
 * 
 */
package org.sagacity.shs.http;

import java.io.IOException;
import java.nio.charset.Charset;

import org.sagacity.shs.ChannelIO;

/**
 * @author lizhitao
 *
 */
public class StringContent implements Content {
	private String errMsg; 
	
	public StringContent(String errMsg){
		this.errMsg = errMsg;
	}

	/* (non-Javadoc)
	 * @see org.sagacity.shs.http.Sendable#prepare()
	 */
	@Override
	public void prepare() throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.sagacity.shs.http.Sendable#send(org.sagacity.shs.ChannelIO)
	 */
	@Override
	public boolean send(ChannelIO io) throws IOException {
		io.write(Charset.forName("utf-8").encode(errMsg));
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sagacity.shs.http.Sendable#release()
	 */
	@Override
	public void release() throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.sagacity.shs.http.Content#type()
	 */
	@Override
	public String type() {
		return "text/html;charset=utf-8";
	}

	/* (non-Javadoc)
	 * @see org.sagacity.shs.http.Content#length()
	 */
	@Override
	public long length() {
		return this.errMsg.length();
	}

}
