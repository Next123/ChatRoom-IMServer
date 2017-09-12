package jk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;

public class ServerThread implements Runnable {
	Socket s = null;

	BufferedReader br = null;
	private String[] array;
	private String name;
	private String text;
	private String time;
	private File file = new File("消息记录.txt");

	FileWriter fw = null;
	PrintWriter pw = null;

	public ServerThread(Socket s) throws IOException {
		this.s = s;

		this.br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
		this.fw = new FileWriter(this.file, true);

		this.pw = new PrintWriter(this.fw);
	}

	public void run() {
		try {
			String content = null;
			Iterator it ;
			for (; (content = readFromClient()) != null; it.hasNext()) {
				this.array = content.split("♥");
				this.name = this.array[0];
				this.text = this.array[2];
				this.time = this.array[3];
				String log = this.time + "  Name: " + this.name + "  Message:" + this.text;
				this.pw.println(log);
				this.pw.flush();
				System.out.println(log);
				it = MyServer.socketList.iterator();
				Socket s = (Socket) it.next();
				try {
					OutputStream os = s.getOutputStream();
					os.write((content + "\n").getBytes("utf-8"));
				} catch (SocketException e) {
					it.remove();
					System.out.println("------------>>用户退出登录<<-------------");
				}
			}

		} catch (IOException e) {
			System.out.println("------------>>向客户端发送数据失败<<-------------");
			try {
				this.fw.flush();
				this.pw.close();
				this.fw.close();
			} catch (IOException localIOException1) {
			}
		} finally {
			try {
				this.fw.flush();
				this.pw.close();
				this.fw.close();
			} catch (IOException localIOException2) {
			}
		}
	}

	private String readFromClient() {
		try {
			return this.br.readLine();
		} catch (IOException e) {
			MyServer.socketList.remove(this.s);
			System.out.println("------------>>读取客户端数据失败<<-------------");
		}
		return null;
	}
}