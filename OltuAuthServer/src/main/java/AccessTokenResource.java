package org.garfield.java.jersey.example.jersey.resource.oauth2.server;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.garfield.java.jersey.example.common.constant.OAuthConstant;
import org.garfield.java.jersey.example.entity.OAuth2ClientInfo;
import org.garfield.java.jersey.example.entity.OAuth2ClientToken;
import org.garfield.java.jersey.example.service.IOAuth2ClientInfoService;
import org.springframework.beans.factory.annotation.Autowired;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("login/oauth/access_token")
@Singleton
public class AccessTokenResource {
    
    @Autowired
    private IOAuth2ClientInfoService oAuth2ClientInfoService;
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response accessToken(@Context HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        
        OAuthTokenRequest oAuthRequest = new OAuthTokenRequest(request);
        OAuthIssuer oAuthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        
        // verify client access token request
        String clientId = oAuthRequest.getClientId();
        OAuth2ClientInfo clientInfo = oAuth2ClientInfoService.findOAuth2ClientInfo(clientId);
        OAuth2ClientToken clientToken = oAuth2ClientInfoService.findOAuth2ClientToken(clientId);
        if(null == clientInfo)
            throw new IllegalArgumentException("invalid clientId : " + clientId);
        else if(null == clientToken)
            throw new IllegalArgumentException("invalid request, need get auth code first!");
        
        String clientSecret = oAuthRequest.getClientSecret();
        if(!StringUtils.equals(clientSecret, clientInfo.getClientSecret()))
            throw new IllegalArgumentException("invalid clientSecret : " + clientSecret);
        String redirectUrl = oAuthRequest.getRedirectURI();
        if(!StringUtils.equals(clientInfo.getRedirectUrl(), redirectUrl))
            throw new IllegalArgumentException("the redirect url doesn't match the register one: " + redirectUrl);
        String grantType = oAuthRequest.getGrantType();
        if(StringUtils.equals(grantType, GrantType.AUTHORIZATION_CODE.toString())) {
            String code = oAuthRequest.getCode();
            if(!StringUtils.equals(clientToken.getOauthCode(), code))
                throw new IllegalArgumentException("invalid auth code");
        } else if(StringUtils.equals(grantType, GrantType.PASSWORD.toString())) {
            oAuthRequest.getUsername();
            oAuthRequest.getPassword();
        } else if(StringUtils.equals(grantType, GrantType.REFRESH_TOKEN.toString())) {
            //TODO how to implement
        }
        
        String accessToken;
        if(null == clientToken.getOauthToken()) {
            clientToken.setOauthToken(oAuthIssuerImpl.accessToken());
            clientToken.setUpdateDate(new Timestamp(new Date().getTime()));
            oAuth2ClientInfoService.updateOAuth2ClientToken(clientToken);
        }
        accessToken = clientToken.getOauthToken();
        OAuthResponse oAuthResponse = OAuthASResponse
                .tokenResponse(Status.OK.getStatusCode())
                .setAccessToken(accessToken)
                .setExpiresIn(OAuthConstant.EXPIRESIN)
                .buildJSONMessage();
        
        return Response.status(oAuthResponse.getResponseStatus()).entity(oAuthResponse.getBody()).build();
    }
}
