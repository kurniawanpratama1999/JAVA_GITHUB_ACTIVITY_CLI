package controllers;

import com.google.gson.*;
import models.GithubEventModel;
import models.UserModel;
import utils.LocalDateTimeAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserEvent {
    private final Set<GithubEventModel> GITHUB = new LinkedHashSet<>();

    public UserEvent (String username) {

        try {
            UserModel userModel = new UserModel(username);
            URL api = new URI(userModel.getEndpoint()).toURL();
            HttpURLConnection http = (HttpURLConnection) api.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/vnd.github.v3+json");

            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            JsonElement jsonElement = JsonParser.parseString(br.readLine());
            JsonArray jsArray = jsonElement.getAsJsonArray();

            for (JsonElement item : jsArray) {
                JsonObject event = item.getAsJsonObject();
                String type = event.get("type").getAsString();

                JsonObject payload = event.get("payload").getAsJsonObject();

                String p = null;
                if (payload.has("commits")) {
                    p = payload
                            .get("commits").getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("message").getAsString();
                }

                String repo = event
                        .get("repo").getAsJsonObject()
                        .get("name").getAsString();

                LocalDateTime createdAt = ZonedDateTime.parse(
                        event.get("created_at").getAsString()
                ).withZoneSameInstant(
                        ZoneId.of("Asia/Jakarta")
                ).toLocalDateTime();

                GithubEventModel githubEventModel = new GithubEventModel(type, repo, p, createdAt);

                GITHUB.add(githubEventModel);
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void getList () {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();

        for (GithubEventModel item : GITHUB) {
            String type = item.getType();
            String payload = item.getPayloadCommitMessage();
            String repo = item.getRepoName();
            LocalDateTime createdAt = item.getCreatedAt();

            if (payload != null) {
                System.out.printf("%s -> %s\nmessage: %s\ncreated at: %s\n",
                        type, repo, payload, createdAt);
            } else {
                System.out.printf("%s -> %s\ncreated at: %s\n",
                        type, repo, createdAt);
            }
            System.out.println("_".repeat(30));
        }
    }

}
