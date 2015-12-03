package org.garfield.java.jersey.example.jersey.resource.oauth2.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.as.response.OAuthASResponse.OAuthAuthorizationResponseBuilder;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.garfield.java.jersey.example.entity.OAuth2ClientInfo;
import org.garfield.java.jersey.example.entity.OAuth2ClientToken;
import org.garfield.java.jersey.example.service.IOAuth2ClientInfoService;
import org.springframework.beans.factory.annotation.Autowired;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("login/oauth/authorize")
@Singleton
public class AuthorizationResource {
    
    @Autowired
    private IOAuth2ClientInfoService oAuth2ClientInfoService;
    
    @GET
    public Response authorize(@Context HttpServletRequest request) throws OAuthSystemException, OAuthProblemException, URISyntaxException {
        
        OAuthAuthzRequest oAuthRequest = new OAuthAuthzRequest(request);
        OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
        
        String state = oAuthRequest.getParam(OAuth.OAUTH_STATE);
        // verify the request
        String responseType = oAuthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
        if(!StringUtils.equalsIgnoreCase(responseType, ResponseType.CODE.toString()))
            throw new IllegalArgumentException("authorize responsetype must be " + ResponseType.CODE.toString());
        String clientId = oAuthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
        OAuth2ClientInfo clientInfo = oAuth2ClientInfoService.findOAuth2ClientInfo(clientId);
        if(null == clientInfo)
            throw new IllegalArgumentException("invalid clientId : " + clientId);
        String redirectUrl = oAuthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
        if(!StringUtils.equals(clientInfo.getRedirectUrl(), redirectUrl))
            throw new IllegalArgumentException("the redirect url doesn't match the register one: " + redirectUrl);
        String scope = oAuthRequest.getParam(OAuth.OAUTH_SCOPE);
        if(!StringUtils.equals(scope, "user"))
            throw new IllegalArgumentException("unsupported scope: " + scope);
        
        String code = oAuthIssuer.authorizationCode();
        OAuth2ClientToken clientToken = new OAuth2ClientToken();
        clientToken.setClientInfo(clientInfo);
        clientToken.setOauthCode(code);
        clientToken.setUpdateDate(new Timestamp(new Date().getTime()));
        oAuth2ClientInfoService.updateOAuth2ClientToken(clientToken);
        
        OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, Status.FOUND.getStatusCode());
        builder.setCode(code);
        builder.setScope(scope);
        builder.setParam("state", null == state ? oAuthIssuer.authorizationCode() : state);
        OAuthResponse oAuthResponse = builder.location(redirectUrl).buildQueryMessage();
        
        return Response.status(oAuthResponse.getResponseStatus()).location(new URI(oAuthResponse.getLocationUri())).build();
    }
}
