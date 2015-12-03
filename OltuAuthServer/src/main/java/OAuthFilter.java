package org.garfield.java.jersey.example.jersey.resource.oauth2;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.garfield.java.jersey.example.common.util.LogExceptionUtil;

import com.alibaba.druid.util.StringUtils;

public class OAuthFilter implements ContainerRequestFilter{
    
    @Context
    private HttpServletRequest request;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if(!(StringUtils.equals(path, "oauth/login") || StringUtils.equals(path, "oauth/register"))) {
            try {
                OAuthAccessResourceRequest oAuthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
                String accessToken = oAuthRequest.getAccessToken();
                // verify token
                
                if(accessToken.equals("")) {
                    OAuthResponse oauthResponse = OAuthRSResponse
                            .errorResponse(Response.Status.UNAUTHORIZED.getStatusCode())
                            .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                            .buildHeaderMessage();
                    Response response = Response.status(Status.UNAUTHORIZED).header(OAuth.HeaderType.WWW_AUTHENTICATE,
                                oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE)).build();
                    throw new WebApplicationException(response);
                }
            } catch (OAuthSystemException e) {
                LogExceptionUtil.logExceptionStackTrace(e);
                throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (OAuthProblemException e) {
                LogExceptionUtil.logExceptionStackTrace(e);
                throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build());
            }
        }
    }

}
