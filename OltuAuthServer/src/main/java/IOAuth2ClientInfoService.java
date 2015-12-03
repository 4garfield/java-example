package org.garfield.java.jersey.example.service;

import java.util.List;

import org.garfield.java.jersey.example.entity.OAuth2ClientInfo;
import org.garfield.java.jersey.example.entity.OAuth2ClientToken;

public interface IOAuth2ClientInfoService {
    
    public List<OAuth2ClientInfo> findAll();
    
    public OAuth2ClientInfo findOAuth2ClientInfo(Long id);
    
    public OAuth2ClientInfo createOAuth2ClientInfo(OAuth2ClientInfo oAuth2ClientInfo);
    
    public OAuth2ClientInfo updateOAuth2ClientInfo(OAuth2ClientInfo oAuth2ClientInfo);
    
    public void deleteOAuth2ClientInfo(Long id);
    
    public boolean existOAuth2ClientInfo(Long id);
    
    public OAuth2ClientInfo findOAuth2ClientInfo(String clientId);
    
    public void updateOAuth2ClientToken(OAuth2ClientToken clientToken);
    
    public OAuth2ClientToken findOAuth2ClientToken(String clientId);
}
