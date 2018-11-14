package com.zdj.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangdanjiang
 * @description
 * @date 2018/11/14
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(10001));
        System.out.println("Server is running...");
        try {
            while (true) {
                new Thread(new ServerTask(serverSocket.accept())).start();
            }
        }finally {
            serverSocket.close();
        }
    }

    private static class ServerTask implements Runnable {
        private Socket task;

        public ServerTask(Socket task) {
            this.task = task;
        }

        @Override
        public void run() {
            try (
                    ObjectOutputStream outputStream = new ObjectOutputStream(task.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(task.getInputStream());
            ) {
                String request =  inputStream.readUTF();
                System.out.println("request msg:"+request);
                outputStream.writeUTF("hello "+request);
                outputStream.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    task.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
