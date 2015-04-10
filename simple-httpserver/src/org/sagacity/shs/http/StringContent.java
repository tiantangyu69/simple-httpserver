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
public class StringContent implements Content {

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
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sagacity.shs.http.Content#length()
	 */
	@Override
	public long length() {
		// TODO Auto-generated method stub
		return 0;
	}

}
