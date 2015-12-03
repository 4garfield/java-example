package org.garfield.java.jersey.example.dao.impl;

import org.garfield.java.jersey.example.dao.IOAuth2ClientInfoDao;
import org.garfield.java.jersey.example.entity.OAuth2ClientInfo;
import org.springframework.stereotype.Repository;

@Repository
public class OAuth2ClientInfoDaoImpl extends BaseDaoImpl<OAuth2ClientInfo, Long> implements IOAuth2ClientInfoDao{
    
    public OAuth2ClientInfoDaoImpl() {
        super(OAuth2ClientInfo.class);
    }
}
