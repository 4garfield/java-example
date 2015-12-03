package org.garfield.java.jersey.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "OAUTH2_CLIENT_INFO")
public class OAuth2ClientInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "APP_NAME", nullable = false, unique = true)
    @NotNull
    private String appName;
    
    @Column(name = "CLIENT_ID", nullable = false, unique = true)
    @NotNull
    private String clientId;
    
    @Column(name = "CLIENT_SECRET", nullable = false, unique = true)
    @NotNull
    private String clientSecret;
    
    @Column(name = "REDIRECT_URL", nullable = false)
    @NotNull
    @Pattern(regexp="^https?://\\w(.\\w)(:\\d+)?(/\\w)*$")
    private String redirectUrl;
    
    @Column(name = "CREATE_DATE", nullable = false)
    private Timestamp createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
}
