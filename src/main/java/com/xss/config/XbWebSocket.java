package com.xss.config;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DZY
 * @date 2020/9/5
 * @desc
 */
@Component
@ServerEndpoint("/xb_socket/{uId}")
public class XbWebSocket {

    public XbWebSocket() {
        System.out.println("webSocket生成啦");
    }

    //在线总人数
    private static int onlineCount = 0;

    //会话session对象
    private static final Map<Long, Session> sessions = new ConcurrentHashMap();

    // 与服务器保持连接的会话对象
    private Session session;



    @OnOpen
    public void OnOpen(Session session,@PathParam("uId") Long uId){

        this.session = session;

        // 把会话对象存储起来
        sessions.put(uId, session);

        // 在线人数+1
        addOnlineCount();

        System.out.println("当前会议人数: " + getOnlineCount());

    }


    @OnClose
    public void OnClose(Session session,@PathParam("uId") Long uId){
        // 把当前用户移除会议
        sessions.remove(uId);

        // 会议人数-1
        subOnlineCount();

        System.out.println("当前会议人数: " + getOnlineCount());
    }


    /**
     * 群发消息
     */
    public static void sendMessage(String message) {

        for (Long userId : sessions.keySet()) {

            Session session = sessions.get(userId);

            session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 单发消息
     *
     * @param userId
     * @param message
     */
    public static void sendMessage(Long userId, String message) {

        Session session = sessions.get(userId);

        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 群发消息(指定某个人不发)
     *
     * @param userId
     * @param message
     */
    public static void sendMessageNotUser(Long userId, String message) {

        for (Long tempId : sessions.keySet()) {

            if (tempId.longValue() != userId.longValue()) {
                Session session = sessions.get(tempId);

                if (session != null) {
                    session.getAsyncRemote().sendText(message);
                }
            }
        }
    }




    //获得在线总人数
    public static int getOnlineCount(){
        return onlineCount;
    }

    //在线人数加一
    public static void addOnlineCount(){
        XbWebSocket.onlineCount++;

    }


    //在线人数减一
    public static void subOnlineCount(){
        XbWebSocket.onlineCount--;
    }






}
