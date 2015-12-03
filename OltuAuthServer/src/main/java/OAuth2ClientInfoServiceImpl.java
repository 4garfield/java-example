package org.garfield.java.jersey.example.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.garfield.java.jersey.example.dao.IOAuth2ClientInfoDao;
import org.garfield.java.jersey.example.dao.IOAuth2ClientTokenDao;
import org.garfield.java.jersey.example.entity.OAuth2ClientInfo;
import org.garfield.java.jersey.example.entity.OAuth2ClientToken;
import org.garfield.java.jersey.example.service.IOAuth2ClientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OAuth2ClientInfoServiceImpl implements IOAuth2ClientInfoService{
    
    private static final MD5Generator md5Generator = new MD5Generator();
    
    @Autowired
    private IOAuth2ClientInfoDao oAuth2ClientInfoDao;
    
    @Autowired
    private IOAuth2ClientTokenDao oAuth2ClientTokenDao;
    
    @Override
    public List<OAuth2ClientInfo> findAll() {
        return oAuth2ClientInfoDao.findAll();
    }
    
    @Override
    public OAuth2ClientInfo findOAuth2ClientInfo(Long id) {
        return oAuth2ClientInfoDao.find(id);
    }

    @Override
    public OAuth2ClientInfo createOAuth2ClientInfo(OAuth2ClientInfo oAuth2ClientInfo) {
        try {
            String encodeBase = oAuth2ClientInfo.getAppName() + UUID.randomUUID();
            String clientId = md5Generator.generateValue(Base64.encodeBase64String(encodeBase.getBytes()));
            oAuth2ClientInfo.setClientId(clientId);
            oAuth2ClientInfo.setCreateDate(new Timestamp(new Date().getTime()));
            oAuth2ClientInfo.setClientSecret(md5Generator.generateValue(clientId + UUID.randomUUID()));
            return oAuth2ClientInfoDao.save(oAuth2ClientInfo);
        } catch (OAuthSystemException e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public OAuth2ClientInfo updateOAuth2ClientInfo(OAuth2ClientInfo oAuth2ClientInfo) {
        String clientId = Base64.encodeBase64String((oAuth2ClientInfo.getAppName() + UUID.randomUUID()).getBytes());
        OAuth2ClientInfo originEntity = oAuth2ClientInfoDao.find(oAuth2ClientInfo.getId());
        // don't allow to modify clientId
        // if change appName, then all the client info will changed.
        originEntity.setRedirectUrl(oAuth2ClientInfo.getRedirectUrl());
        try{
            if(StringUtils.equalsIgnoreCase(originEntity.getAppName(), oAuth2ClientInfo.getAppName())) {
                originEntity.setClientSecret(md5Generator.generateValue(clientId + UUID.randomUUID()));
            } else {
                originEntity.setClientId(clientId);
                originEntity.setClientSecret(md5Generator.generateValue(clientId + UUID.randomUUID()));
            }
        } catch (OAuthSystemException e) {
            throw new IllegalStateException(e);
        }
        return oAuth2ClientInfoDao.update(originEntity);
    }
    
    @Override
    public void deleteOAuth2ClientInfo(Long id) {
        oAuth2ClientInfoDao.delete(id);
    }
    
    @Override
    public boolean existOAuth2ClientInfo(Long id) {
        return oAuth2ClientInfoDao.existEntity(id);
    }
    
    @Override
    public OAuth2ClientInfo findOAuth2ClientInfo(String clientId) {
        return oAuth2ClientInfoDao.findByUniqueField("clientId", clientId);
    }
    
    @Override
    public void updateOAuth2ClientToken(OAuth2ClientToken clientToken) {
        oAuth2ClientTokenDao.update(clientToken);
    }
    
    @Override
    public OAuth2ClientToken findOAuth2ClientToken(String clientId) {
        return oAuth2ClientTokenDao.findByClientId(clientId);
    }
}
