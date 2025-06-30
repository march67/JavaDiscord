// ApiConstants.java
package com.javadiscordproject.utils;

public class ApiConstants {
    public static final String BASE_URL = "http://localhost:8080/api/";
    public static final String USERS_ENDPOINT = BASE_URL + "user/register";
    public static final String AUTHENTICATE_ENDPOINT = BASE_URL + "authenticate";
    public static final String FRIEND_LIST_ENDPOINT = BASE_URL + "friendlist/friendlist";
    public static final String PENDING_FRIEND_REQUESTS_ENDPOINT = BASE_URL + "friendlist/pending-friend-requests";

    private ApiConstants() {
    }
}