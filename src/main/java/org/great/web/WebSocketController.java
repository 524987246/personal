package org.great.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.great.util.myutil.MyUserUtils;
import org.springframework.stereotype.Component;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *                 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */

@ServerEndpoint(value = "/websocket/{param}")
@Component
public class WebSocketController {
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<WebSocketController>();
	private static ConcurrentMap<Long, WebSocketController> webSocketMap = new ConcurrentHashMap<Long, WebSocketController>();

	private List<WebSocketController> list = new ArrayList<>();
	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "param") String param, Session session) {
		this.session = session;
		webSocketSet.add(this); // 加入set中
		Long id = MyUserUtils.getLoginUser(session.getUserPrincipal(), "object").getId();
		webSocketMap.put(id, this);
		addOnlineCount(); // 在线数加1
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session) {
		webSocketSet.remove(this); // 从set中删除
		Long id = MyUserUtils.getLoginUser(session.getUserPrincipal(), "object").getId();
		webSocketMap.remove(id);
		subOnlineCount(); // 在线数减1
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 * @param session
	 *            可选的参数
	 */
	@OnMessage
	public void onMessage(String message) {
		System.out.println("来自客户端的消息:" + message + "  待处理");
		// // 群发消息
		// for (WebSocketController item : webSocketSet) {
		// try {
		// item.sendMessage(message);
		// } catch (IOException e) {
		// e.printStackTrace();
		// continue;
		// }
		// }
	}

	public void autoMessage(String message) {
		System.out.println("来自客户端的消息:" + message);
		// 群发消息
		for (WebSocketController item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 对特定 中的用户推送消息
	 * 
	 * @param message
	 * @param longs
	 *            用户id集合
	 */
	public void oneMessage(String message, Long... longs) {
		System.out.println("来自客户端的消息:" + message);
		if (longs == null || longs.length == 0) {
			System.out.println("无特定用户发送");
			return;
		}
		// 特定用户发送
		for (Number num : longs) {
			WebSocketController item = webSocketMap.get(num);
			if (item != null) {
				try {
					item.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		webSocketSet.remove(this); // 从set中删除
		Long id = MyUserUtils.getLoginUser(session.getUserPrincipal(), "object").getId();
		webSocketMap.remove(id);
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
		// this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketController.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketController.onlineCount--;
	}
}