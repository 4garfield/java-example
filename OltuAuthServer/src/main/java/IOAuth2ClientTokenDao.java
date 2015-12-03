package org.garfield.java.jersey.example.dao;

import org.garfield.java.jersey.example.entity.OAuth2ClientToken;

public interface IOAuth2ClientTokenDao extends IBaseDao<OAuth2ClientToken, Long>{

    public OAuth2ClientToken findByClientId(String clientId);
}
