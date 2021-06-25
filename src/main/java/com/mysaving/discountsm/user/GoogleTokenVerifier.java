package com.mysaving.discountsm.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenVerifier {

  public static final String CLIENT_ID = "28404597374-8qv4b00mmc2ur41mrqs7n5rcdekt7v2p.apps.googleusercontent.com";

  Optional<Payload> validateToken(String clientToken) throws GeneralSecurityException, IOException {
    NetHttpTransport transport = new NetHttpTransport();
    GsonFactory jsonFactory = new GsonFactory();
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();
    GoogleIdToken idToken = verifier.verify(clientToken);

    if (idToken != null) {
      return Optional.of(idToken.getPayload());
    }

    System.out.println("Invalid ID token.");
    return Optional.empty();

  }
}
