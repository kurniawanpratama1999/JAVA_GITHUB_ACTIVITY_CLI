package models;

import java.time.LocalDateTime;

public class UserModel {
    private final String username;
    private final String endpoint;

    public UserModel(String username) {
        this.username = username;
        this.endpoint = String.format("https://api.github.com/users/%s/events", username);
    }

    public String getUsername() {
        return username;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
