package com.lookatme.server.member.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
@Getter
public class Account {

    @Column(updatable = false)
    private String email;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private OauthPlatform oauthPlatform;

    protected Account() {
    }

    public Account(String email) {
        this.email = email;
        this.oauthPlatform = OauthPlatform.NONE;
    }

    public Account(String email, OauthPlatform oauthPlatform) {
        this.email = email;
        this.oauthPlatform = oauthPlatform == null ? OauthPlatform.NONE : oauthPlatform;
    }

    @Override
    public String toString() {
        return String.format("%s/%s", email, oauthPlatform.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return email.equals(account.email) && oauthPlatform == account.oauthPlatform;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, oauthPlatform);
    }
}
