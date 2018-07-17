package io.netty.tcp.testor.tcp.fullduplex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * 服务器，全双工，支持单播和广播
 *
 * 注意是全双工，全双工，全双工
 * 
 * 就是像QQ一样
 */
public class Server {
	// 分配给socket连接的id，用于区分不同的socket连接
	private static int id = 0;
	// 存储socket连接，发送消息的时候从这里取出对应的socket连接
	private HashMap<Integer, ServerThread> socketList = new HashMap<>();
	// 服务器对象，用于监听TCP端口
	private ServerSocket server;

	/**
	 * 构造函数，必须输入端口号
	 */
	public Server(int port) {
		try {
			this.server = new ServerSocket(port);
			System.out.println("服务器启动完成 使用端口: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动服务器，先让Writer对象启动等待键盘输入，然后不断等待客户端接入 如果有客户端接入就开一个服务线程，并把这个线程放到Map中管理
	 */
	public void start() {
		new Writer().start();
		try {
			while (true) {
				Socket socket = server.accept();
				System.out.println(++id + ":客户端接入:" + socket.getInetAddress() + ":" + socket.getPort());
				ServerThread thread = new ServerThread(id, socket);
				socketList.put(id, thread);
				thread.run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 回收资源啦，虽然不广播关闭也没问题，但总觉得通知一下客户端比较好
	 */
	public void close() {
		sendAll("exit");
		try {
			if (server != null) {
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * 遍历存放连接的Map，把他们的id全部取出来，注意这里不能直接遍历Map，不然可能报错 报错的情况是，当试图发送 `*:exit`
	 * 时，这段代码会遍历Map中所有的连接对象，关闭并从Map中移除 java的集合类在遍历的过程中进行修改会抛出异常
	 */
	public void sendAll(String data) {
		LinkedList<Integer> list = new LinkedList<>();
		Set<Map.Entry<Integer, ServerThread>> set = socketList.entrySet();
		for (Map.Entry<Integer, ServerThread> entry : set) {
			list.add(entry.getKey());
		}
		for (Integer id : list) {
			send(id, data);
		}
	}

	/**
	 * 单播
	 */
	public void send(int id, String data) {
		ServerThread thread = socketList.get(id);
		thread.send(data);
		if ("exit".equals(data)) {
			thread.close();
		}
	}

	// 服务线程，当收到一个TCP连接请求时新建一个服务线程
	private class ServerThread implements Runnable {
		private int id;
		private Socket socket;
		private InputStream in;
		private OutputStream out;
		private PrintWriter writer;

		/**
		 * 构造函数
		 * 
		 * @param id
		 *            分配给该连接对象的id
		 * @param socket
		 *            将socket连接交给该服务线程
		 */
		ServerThread(int id, Socket socket) {
			try {
				this.id = id;
				this.socket = socket;
				this.in = socket.getInputStream();
				this.out = socket.getOutputStream();
				this.writer = new PrintWriter(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 因为设计为全双工模式，所以读写不能阻塞，新开线程进行读操作
		 */
		@Override
		public void run() {
			new Reader().start();
		}

		/**
		 * 因为同时只能有一个键盘输入，所以输入交给服务器管理而不是服务线程
		 * 服务器负责选择socket连接和发送的消息内容，然后调用服务线程的write方法发送数据
		 */
		public void send(String data) {
			if (!socket.isClosed() && data != null && !"exit".equals(data)) {
				writer.println(data);
				writer.flush();
			}
		}

		/**
		 * 关闭所有资源
		 */
		public void close() {
			try {
				if (writer != null) {
					writer.close();
				}
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (socket != null) {
					socket.close();
				}
				socketList.remove(id);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 因为全双工模式所以将读操作单独设计为一个类，然后开个线程执行
		 */
		private class Reader extends Thread {
			private InputStreamReader streamReader = new InputStreamReader(in);
			private BufferedReader reader = new BufferedReader(streamReader);

			@Override
			public void run() {
				try {
					String line = "";
					// 只要连接没有关闭，而且读到的行不为空，为空说明连接异常断开，而且客户端发送的不是exit，那么就一直从连接中读
					while (!socket.isClosed() && line != null && !"exit".equals(line)) {
						line = reader.readLine();
						if (line != null) {
							System.out.println(id + ":client: " + line);
						}
					}
					// 如果循环中断说明连接已断开
					System.out.println(id + ":客户端主动断开连接");
					close();
				} catch (IOException e) {
					System.out.println(id + ":连接已断开");
				} finally {
					try {
						if (streamReader != null) {
							streamReader.close();
						}
						if (reader != null) {
							reader.close();
						}
						close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 因为发送的时候必须指明发送目的地，所以不能交给服务线程管理写操作，不然就无法选择向哪个连接发送消息
	 * 如果交给服务线程管理的话，Writer对象的会争夺键盘这一资源，谁抢到是谁的，就无法控制消息的发送对象了
	 */
	private class Writer extends Thread {
		// 我们要从键盘获取发送的消息
		private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		@Override
		public void run() {
			String line = "";
			// 先来个死循环，除非主动输入exit关闭服务器，否则一直等待键盘写入
			while (true) {
				try {
					line = reader.readLine();
					if ("exit".equals(line)) {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 输入是有规则的 [连接id]:[要发送的内容]
				// 连接id可以为*，代表所有的连接对象，也就是广播
				// 要发送的内容不能为空，发空内容没意义，而且浪费流量
				// 连接id和要发送的消息之间用分号分割，注意是半角的分号
				// 例如： 1:你好 ==>客户端看到的是 server:你好
				// *:吃饭了 ==>所有客户端都能看到 server:吃饭了
				if (line != null) {
					try {
						String[] data = line.split(":");
						if ("*".equals(data[0])) {
							// 这里是广播
							sendAll(data[1]);
						} else {
							// 这里是单播
							send(Integer.parseInt(data[0]), data[1]);
						}
						// 有可能发生的异常
					} catch (NumberFormatException e) {
						System.out.print("必须输入连接id号");
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.print("发送的消息不能为空");
					} catch (NullPointerException e) {
						System.out.print("连接不存在或已经断开");
					}
				}
			}
			// 循环中断说明服务器退出运行
			System.out.println("服务器退出");
			close();
		}
	}

	public static void main(String[] args) {
//		int port = Integer.parseInt(args[0]);
		int port = 8880;
		new Server(port).start();
	}
}