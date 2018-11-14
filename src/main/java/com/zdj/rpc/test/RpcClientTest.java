package com.zdj.rpc.test;

import com.zdj.rpc.client.RpcClientFrame;
import com.zdj.service.UserService;

import java.net.InetSocketAddress;

/**
 * @author zhangdanjiang
 * @description
 * @date 2018/11/14
 */
public class RpcClientTest {
    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
        UserService userService = RpcClientFrame.getRemoteProxyObj(UserService.class, address);
        userService.addUser("zdj", "123456");
    }
}
