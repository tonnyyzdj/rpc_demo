package com.zdj.rpc.client;

import com.sun.corba.se.impl.io.OutputStreamHook;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zhangdanjiang
 * @description
 * @date 2018/11/14
 */
public class RpcClientFrame {

    //远程代理对象，通过动态代理
    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface, final InetSocketAddress address){
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, new DynProxy(serviceInterface, address));
    }

    //动态代理类
    private static class DynProxy implements InvocationHandler{
        private final Class<?> serviceInterface ;
        private final InetSocketAddress address;

        public DynProxy(Class<?> serviceInterface, InetSocketAddress address) {
            this.serviceInterface = serviceInterface;
            this.address = address;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Socket socket = null;
            ObjectInputStream inputStream = null;
            ObjectOutputStream outputStream = null;
            try{
                socket = new Socket();
                socket.connect(address);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                //发送客户端的调用请求
                outputStream.writeUTF(serviceInterface.getName());
                outputStream.writeUTF(method.getName());
                outputStream.writeObject(args);
                outputStream.writeObject(method.getParameterTypes());
                outputStream.flush();
                //接收服务器的应答
                inputStream = new ObjectInputStream(socket.getInputStream());
                return inputStream.readObject();
            }finally {
                if (socket != null) {
                    socket.close();
               }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }
}
