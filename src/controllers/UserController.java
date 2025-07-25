package controllers;

import com.google.gson.*;
import models.GithubEventModel;
import models.UserModel;
import utils.ResMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserController {
    private final Set<GithubEventModel> GITHUB = new LinkedHashSet<>();

    public ResMessage<Set<GithubEventModel>> getList (String username) {
        try {
            UserModel userModel = new UserModel(username);
            URL api = new URI(userModel.getEndpoint()).toURL();
            HttpURLConnection http = (HttpURLConnection) api.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/vnd.github.v3+json");

                if (http.getResponseCode() == 200) {
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

                    return ResMessage.ok("Berhasil", GITHUB);
                }
                else if (http.getResponseCode() == 304) return ResMessage.fail("Not modified");
                else if (http.getResponseCode() == 403) return ResMessage.fail("Forbidden");
                else if (http.getResponseCode() == 504) return ResMessage.fail("Service unavailable");
                else return ResMessage.fail("Nothing");
        } catch (URISyntaxException | IOException e) {
            return ResMessage.err("Connection Failed");
        }
    }

    /*public void getList () {
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
    }*/

}
