/**
 * netty-tcp.
 * Copyright (C) 1999-2017, All rights reserved.
 *
 * This program and the accompanying materials are under the terms of the Apache License Version 2.0.
 */
package com.iceps.spring.nettytcp;

import java.util.Map;

import io.netty.channel.Channel;
import io.netty.tcp.server.ServiceAppHandler;

/**
 * .
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public class MapMesgHandler implements ServiceAppHandler {

	/**
	 * 
	 */
	public MapMesgHandler() {
		// TODO 自动生成的构造函数存根
	}


	/* (non-Javadoc)
	 * @see com.csii.ccbs.tcp.netty.server.ServiceAppHandler#call(java.lang.Object, io.netty.channel.Channel)
	 */
	@Override
	public Object call(Object request, Channel channel) {
		Map map = (Map) request;
		map.put("ServerTimeStamp", System.currentTimeMillis());
//		System.out.println(">" + map);
		System.out.println(Thread.currentThread().getName() + " > " + map);
		return map;
	}


	/* (non-Javadoc)
	 * @see com.csii.ccbs.tcp.netty.server.ServiceAppHandler#exceptionCaught(io.netty.channel.Channel, java.lang.Throwable)
	 */
	@Override
	public void onChannelException(Channel channel, Throwable cause) {
		// TODO 自动生成的方法存根
		
	}


	/* (non-Javadoc)
	 * @see com.csii.ccbs.tcp.netty.server.ServiceAppHandler#onChannelClosed(io.netty.channel.Channel)
	 */
	@Override
	public void onChannelClosed(Channel channel) {
		// TODO 自动生成的方法存根
		
	}

}
