package com.itermit.railway.dao.entity;

import java.io.Serializable;

public class User implements Serializable {
    public static final String F_ID = "id";
    public static final String F_NAME = "name";
    public static final String F_PASSWORD = "password";
    public static final String F_ISADMIN = "isadmin";

    private int id;
    private String name;
    private String password;
    private boolean isadmin;

    private User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsAdmin() {
        return isadmin;
    }

    public void setIsAdmin(boolean isadmin) {
        this.isadmin = isadmin;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", isadmin='" + isadmin + '\'' +
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

        public Builder withIsAdmin(boolean isadmin) {
            user.setIsAdmin(isadmin);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
