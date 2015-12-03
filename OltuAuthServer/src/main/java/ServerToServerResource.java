package org.garfield.java.jersey.example.jersey.resource.oauth2;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.oauth2.jwt.io.JWTClaimsSetWriter;

public class ServerToServerResource {
    
    public void serverToServer() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(new File("P12_FILE")), "notasecret".toCharArray());
        PrivateKey privateKey = (PrivateKey) keyStore.getKey("privatekey", "notasecret".toCharArray());
//        PrivateKey pk = new PrivateKey(privateKey);
        JWT jwt = new JWT.Builder()
        .setClaimsSetIssuer("")
        .setClaimsSetCustomField("scope", "")
        .setClaimsSetAudience("")
        .setClaimsSetIssuedAt(System.currentTimeMillis() / 1000)
        .setClaimsSetExpirationTime(System.currentTimeMillis() / 1000 +3600)
        .build();
        String payload = new JWTClaimsSetWriter().write(jwt.getClaimsSet());
        /*JWS jws = new JWS.Builder()
        .setType("JWT")
        .setAlgorithm(JwsConstants.RS256)
        .setPayload(payload).sign(new SignatureMethodRSAImpl(JwsConstants.RS256), pk).build();
        System.out.println("your assertion is "+new JWSWriter().write(jws));*/
    }
}
