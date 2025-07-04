package models;

import java.time.LocalDateTime;

public class GithubEventModel {
    private final String type;
    private final String repoName;
    private final String payloadCommitMessage;
    private final LocalDateTime createdAt;

    public GithubEventModel(String type, String repoName, String payloadCommitMessage, LocalDateTime createdAt) {
        this.type = type;
        this.repoName = repoName;
        this.payloadCommitMessage = payloadCommitMessage;
        this.createdAt = createdAt;
    }

    public String getType() {return type;}
    public String getRepoName() {return repoName;}
    public String getPayloadCommitMessage() {return payloadCommitMessage;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
