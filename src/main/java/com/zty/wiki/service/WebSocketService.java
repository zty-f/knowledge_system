/**
 * @author: zty
 * @program: knowledge_system
 * @ClassName WebSocketService
 * @description:
 * @create: 2022-01-07 19:49
 * @Version 1.0
 **/
package com.zty.wiki.service;

import com.zty.wiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WebSocketService {
    @Resource
    private WebSocketServer webSocketServer;

    @Async //异步化执行
    public void sendInfo(String info){
        webSocketServer.sendInfo(info);
    }

}
