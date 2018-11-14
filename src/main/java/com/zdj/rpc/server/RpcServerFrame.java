package com.zdj.rpc.server;

import javax.annotation.security.RunAs;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangdanjiang
 * @description rpc框架的服务端部分
 * 一个RPC框架需要解决哪些问题
 * 1.服务调用问题（服务的注册和发现）
 * 2.远程代理问题
 * 3.通信问题
 * 4.序列化问题
 * 5.注册服务的实例化
 * @date 2018/11/14
 */
public class RpcServerFrame {
    //服务的持有容器，key=服务名，Class=实际的服务的Class类
    private static final Map<String, Class> serviceHolder = new HashMap<>();

    //服务的注册
    public void regService(Class serviceInterface,Class impl){
        serviceHolder.put(serviceInterface.getName(), impl);
    }

    //服务的端口号
    private int port;

    public RpcServerFrame(int port) {
        this.port = port;
    }

    //处理服务请求任务
    private static class ServerTask implements Runnable{

        private Socket client = null;

        public ServerTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (
                    ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                    ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
            ) {
                //处理客户端的请求
                String serivceName = inputStream.readUTF();
                String methodName = inputStream.readUTF();
                Object[] arges = (Object[])inputStream.readObject();
                Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();

                //找到实际的服务
                Class serviceClass = serviceHolder.get(serivceName);
                if (serviceClass == null){
                    throw new ClassNotFoundException(serivceName + "not found");
                }
                //进行业务处理，并返回结果
                Method method = serviceClass.getMethod(methodName, paramTypes);
                Object result = method.invoke(serviceClass.newInstance(), arges);


                outputStream.writeObject(result);
                outputStream.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //启动rpc服务
    public  void startService() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("server is start on "+ port);
        try{
            while (true){
                new Thread(new ServerTask(serverSocket.accept())).start();
            }
        }finally {
            serverSocket.close();
        }

    }

}
