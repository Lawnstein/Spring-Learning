package io.netty.tcp.testor.tcp.fullduplex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 客户端 全双工 但同时只能连接一台服务器
 */
public class Client {
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private boolean splited = true;

	/**
	 * 启动客户端需要指定地址和端口号
	 */
	private Client(String address, int port) {
		try {
			socket = new Socket(address, port);
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("客户端启动成功");
	}

	public void start() {
		// 和服务器不一样，客户端只有一条连接，能省很多事
		Reader reader = new Reader();
		Writer writer = new Writer(splited);
		reader.start();
		writer.start();
	}

	public void close() {
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (socket != null) {
				socket.close();
			}
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Reader extends Thread {
		private InputStreamReader streamReader = new InputStreamReader(in);
		private BufferedReader reader = new BufferedReader(streamReader);

		@Override
		public void run() {
			try {
				String line = "";
				while (!socket.isClosed() && line != null && !"exit".equals(line)) {
					line = reader.readLine();
					if (line != null) {
						System.out.println("Server: " + line);
					}
				}
				System.out.println("服务器主动断开连接");
				close();
			} catch (IOException e) {
				System.out.println("连接已断开");
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

	private class Writer extends Thread {
		private PrintWriter writer = new PrintWriter(out);
		private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		private boolean splited = false;
		private List<String> sections = null;
		private int wrts = 8;
		private CountDownLatch latch = null;
		private Thread[] ths = null;

		public Writer(boolean splited) {
			this.splited = splited;
		}

		private CountDownLatch getLatch() {
			return latch;
		}

		private void init() {
			if (!splited)
				return;
			sections = new ArrayList<String>();
			ths = new Thread[wrts];
//			latch = new CountDownLatch(1);
			for (int i = 0; i < wrts; i++) {
				final int k = i;
				ths[i] = new Thread(new Runnable() {

					@Override
					public void run() {
						int r = k;
						CountDownLatch la = null;
						while (true) {
							if (getLatch() != null && getLatch() != la) {
								la = getLatch();
								try {
									la.await();
								} catch (InterruptedException e) {
								}
								for (int j = r; j < sections.size();) {
									if (sections.get(j) == null) {
										return;
									}
									writer.println(r + "[" + sections.get(j) + "]");
									writer.flush();
									sections.set(j, null);
									j += wrts;
									if (j > sections.size()) {
										break;
									}
								}
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							}
						}
					}
				});

			}
			for (int i = 0; i < wrts; i++) {
				ths[i].start();
			}
		}

		private void send(String line) {
			if (!splited) {
				writer.println(line);
				writer.flush();
				return;
			}
			latch = new CountDownLatch(1);
			sections.clear();
			while (line.length() > 0) {
				int l = line.length();
				sections.add(l > 4 ? line.substring(0, 4) : line);
				line = l > 4 ? line.substring(4) : "";
			}
			latch.countDown();
		}

		@Override
		public void run() {
			init();
			try {
				String line = "";
				while (!socket.isClosed() && line != null && !"exit".equals(line)) {
					line = reader.readLine();
					if ("".equals(line)) {
						System.out.print("发送的消息不能为空");
					} else {
						send(line);
						// writer.println(line);
						// writer.flush();
					}
				}
				System.out.println("客户端退出");
				close();
			} catch (IOException e) {
				System.out.println("error:连接已关闭");
			} finally {
				try {
					if (writer != null) {
						writer.close();
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

	public static void main(String[] args) {
		// String address = args[0];
		// int port = Integer.parseInt(args[1]);

		String address = "127.0.0.1";
		int port = 8880;
		new Client(address, port).start();
	}
}