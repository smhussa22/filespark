package com.filespark.client;

public class AppSession {
 
    private static User user;
    private static String token;    

    private AppSession(){}

    public static void login(User u, String t){

        user = u;
        token = t;

    }

    public static User getUser() { return user; }
    public static String getToken() { return token; }
    public static boolean isLoggedIn() { return (user != null && token != null); }

}
