package org.garfield.java.jersey.example.jersey.resource.oauth2;

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

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.JSONHttpServletRequestWrapper;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.OAuthServerRegistrationRequest;
import org.apache.oltu.oauth2.ext.dynamicreg.server.response.OAuthServerRegistrationResponse;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("register")
@Singleton
public class ClientRegisterResource {
    
    @POST
    public Response register(@Context HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        OAuthServerRegistrationRequest oauthRequest = new OAuthServerRegistrationRequest(new JSONHttpServletRequestWrapper(request));
        oauthRequest.discover();
        oauthRequest.getClientName();
        oauthRequest.getClientUrl();
        oauthRequest.getClientDescription();
        oauthRequest.getRedirectURI();
        
        OAuthResponse response = OAuthServerRegistrationResponse
                .status(Status.OK.getStatusCode())
                .setClientId("")
                .setClientSecret("")
                .setIssuedAt("")
                .setExpiresIn("")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }
}
