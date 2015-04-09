/**
 * 
 */
package org.sagacity.shs.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author lizhitao
 * @action 处理非阻塞事件的接口
 */
public interface Handler {
	void handler(SelectionKey key) throws IOException;
}
