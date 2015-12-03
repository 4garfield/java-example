package org.garfield.java.jersey.example.service.impl;

import org.garfield.java.jersey.example.entity.OAuth2ClientInfo;
import org.garfield.java.jersey.example.initial.config.SpringConfig;
import org.garfield.java.jersey.example.service.IOAuth2ClientInfoService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@WebAppConfiguration
public class OltuOAuth2ClientTest {
    
    @Autowired
    private IOAuth2ClientInfoService oAuth2ClientInfoService;
    
    @Test
    @Ignore
    public void testGenerateClientAuth() {
        OAuth2ClientInfo oAuth2ClientInfo = new OAuth2ClientInfo();
        oAuth2ClientInfo.setAppName("Jersey-API-Server-Example");
        oAuth2ClientInfo.setRedirectUrl("http://localhost:8080/JerseyExample/login/oltu/oauth2callback");
        oAuth2ClientInfoService.createOAuth2ClientInfo(oAuth2ClientInfo);
    }
}
