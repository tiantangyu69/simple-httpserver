/**
 * 
 */
package org.sagacity.shs.http;

/**
 * @author lizhitao
 * 
 */
public interface Content extends Sendable {
	/**
	 * 正文的类型
	 * 
	 * @return
	 */
	String type();

	/**
	 * 返回正文的长度，在正文还没有准备之前，即还没有调用prepare方法之前，返回-1
	 * 
	 * @return
	 */
	long length();
}
