package org.garfield.java.jersey.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OAUTH2_CLIENT_TOKEN")
public class OAuth2ClientToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLIENT_INFO_ID", nullable = false, unique = true)
    private OAuth2ClientInfo clientInfo;
    
    @Column(name = "OAUTH_CODE", nullable = false)
    @NotNull
    private String oauthCode;
    
    @Column(name = "OAUTH_TOKEN", nullable = false)
    @NotNull
    private String oauthToken;
    
    @Column(name = "UPDATE_DATE", nullable = false)
    @NotNull
    private Timestamp updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OAuth2ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(OAuth2ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getOauthCode() {
        return oauthCode;
    }

    public void setOauthCode(String oauthCode) {
        this.oauthCode = oauthCode;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
    
}
