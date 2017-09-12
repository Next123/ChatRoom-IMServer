package jk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer {
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
	private static ServerSocket ss;

	public static void main(String[] args) throws IOException {
		ss = new ServerSocket(30000);
		while (true) {
			Socket s = ss.accept();
			socketList.add(s);

			new Thread(new ServerThread(s)).start();
		}
	}
}