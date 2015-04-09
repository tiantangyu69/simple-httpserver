/**
 * 
 */
package org.sagacity.shs.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import org.sagacity.shs.ChannelIO;

/**
 * @author lizhitao
 * 
 */
public class RequestHander implements Handler {
	private ChannelIO channelIO;

	public RequestHander(ChannelIO channelIO) {
		this.channelIO = channelIO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sagacity.shs.handler.Handler#handler(java.nio.channels.SelectionKey)
	 */
	@Override
	public void handler(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub

	}

}
