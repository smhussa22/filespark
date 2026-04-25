package com.filespark.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filespark.AppState;

public class OAuthCallbackHandler {

    private OAuthCallbackHandler(){}

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void handleHandoff(String body) throws Exception {

        try {

            SessionResponse session = mapper.readValue(body, SessionResponse.class);
            if (session == null || session.token == null || session.user == null) {

                throw new IllegalArgumentException("Missing token or user in handoff payload");

            }
            AppSession.login(session.user, session.token);
            AppStateManager.set(AppState.LOGGED_IN);

        }
        catch (Exception e) {

            AppStateManager.set(AppState.LOGGED_OUT);
            throw e;

        }

    }

}
