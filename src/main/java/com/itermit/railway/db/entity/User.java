package com.itermit.railway.db.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * User entity.
 *
 * @author O.Savinov
 */
public class User implements Serializable {

    private int id;
    private String name;
    private String password;
    private String email;
    private boolean isAdmin;
    private boolean isActive;
    private String activationToken;

    /**
     * Returns encrypted password string.
     *
     * @param password String with password
     * @return String with encrypted password
     * @throws NoSuchAlgorithmException
     */
    public static String passwordEncrypt(String password) throws NoSuchAlgorithmException {

        MessageDigest md;

        md = MessageDigest.getInstance("SHA-256");

        md.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        return String.format("%064x", new BigInteger(1, digest));
    }

    /**
     * Generates and sets activation token string for self.
     */
    public void generateActivateToken() {
        final SecureRandom secureRandom = new SecureRandom();
        final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        setActivationToken(base64Encoder.encodeToString(randomBytes));
    }

    private User() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isactive) {
        this.isActive = isactive;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isadmin='" + isAdmin + '\'' +
                ", isactive='" + isActive + '\'' +
                ", activation_token='" + activationToken + '\'' +
                '}';
    }

    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder withId(int id) {
            user.setId(id);
            return this;
        }

        public Builder withName(String name) {
            user.setName(name);
            return this;
        }

        public Builder withPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder withEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder withIsAdmin(boolean isAdmin) {
            user.setIsAdmin(isAdmin);
            return this;
        }

        public Builder withIsActive(boolean isActive) {
            user.setIsActive(isActive);
            return this;
        }

        public Builder withActivationToken(String activationToken) {
            user.setActivationToken(activationToken);
            return this;
        }

        public User build() {
            return user;
        }
    }

}
