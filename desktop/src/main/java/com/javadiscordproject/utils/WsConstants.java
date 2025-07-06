package com.javadiscordproject.utils;

public class WsConstants {
    public static final String WEBSOCKET_URL = "wss://discord2-back-17feecd5a4fc.herokuapp.com/api/ws";
    //public static final String WEBSOCKET_URL = "ws://localhost:8080/api/ws";

    public static final String TOPIC_MESSAGES = "/topic/messages";
    public static final String TOPIC_FRIEND_NOTIFICATION = "/topic/notification";
    public static final String TOPIC_NOTIFICATION_FRIEND = "/topic/notification/friend";
    public static final String APP_FRIEND_NOTIFICATION_ENDPOINT = "/app/notification";
    public static final String APP_NOTIFICATION_FRIEND_ENDPOINT = "/app/notification/friend";
    public static final String APP_CHAT_ENDPOINT = "/app/chat/";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TIMESTAMP_FORMAT = "HH:mm:ss";
}