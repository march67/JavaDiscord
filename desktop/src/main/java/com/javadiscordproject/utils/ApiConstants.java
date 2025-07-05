package com.javadiscordproject.utils;

public class ApiConstants {
    public static final String BASE_URL = "https://discord2-back-17feecd5a4fc.herokuapp.com/api/";

    public static final String USERS_ENDPOINT = BASE_URL + "user/register";
    public static final String AUTHENTICATE_ENDPOINT = BASE_URL + "authenticate";
    public static final String FRIEND_LIST_ENDPOINT = BASE_URL + "friendlist";
    public static final String PENDING_FRIEND_REQUESTS_ENDPOINT = BASE_URL + "friendlist/pending-friend-requests";
    public static final String ADD_FRIEND_ENDPOINT = BASE_URL + "friendlist/add-friend/by-username";
    public static final String ACCEPT_FRIEND_REQUEST_ENDPOINT = BASE_URL + "friendlist/accept";
    public static final String REFUSE_FRIEND_REQUEST_ENDPOINT = BASE_URL + "friendlist/refuse";
    public static final String MESSAGE_ENDPOINT = BASE_URL + "message";

    private ApiConstants() {
    }
}