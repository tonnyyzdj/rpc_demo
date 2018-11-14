package com.zdj.rpc.test;

import com.zdj.rpc.server.RpcServerFrame;
import com.zdj.service.UserService;
import com.zdj.service.impl.UserServiceImpl;

/**
 * @author zhangdanjiang
 * @description
 * @date 2018/11/14
 */
public class RpcServiceTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RpcServerFrame serverFrame = new RpcServerFrame(9999);
                    serverFrame.regService(UserService.class, UserServiceImpl.class);
                    serverFrame.startService();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
