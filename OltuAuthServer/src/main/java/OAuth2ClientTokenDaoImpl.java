package org.garfield.java.jersey.example.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.garfield.java.jersey.example.dao.IOAuth2ClientTokenDao;
import org.garfield.java.jersey.example.entity.OAuth2ClientToken;
import org.springframework.stereotype.Repository;

@Repository
public class OAuth2ClientTokenDaoImpl extends BaseDaoImpl<OAuth2ClientToken, Long> implements IOAuth2ClientTokenDao{
    
    public OAuth2ClientTokenDaoImpl() {
        super(OAuth2ClientToken.class);
    }

    @Override
    public OAuth2ClientToken findByClientId(String clientId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("clientId", clientId);
        return super.createJpaEntityQuery("from OAuth2ClientToken clientToken where clientToken.clientInfo.clientId=:clientId", paramMap)
                .getSingleResult();
    }
}
