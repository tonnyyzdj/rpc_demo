package com.zdj.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zhangdanjiang
 * @description  客户端
 * @date 2018/11/14
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;

        InetSocketAddress address = new InetSocketAddress("127.0.0.1",10001);
        try{
            socket = new Socket();
            socket.connect(address);

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeUTF("zdj1");
            outputStream.flush();

            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println(inputStream.readUTF());

        }finally {
            if(socket!=null)
                socket.close();
            if(outputStream!=null)
                outputStream.close();
            if(inputStream!=null)
                inputStream.close();
        }
    }
}
