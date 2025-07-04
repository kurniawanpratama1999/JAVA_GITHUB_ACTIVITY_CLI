package services;

import controllers.UserController;
import models.GithubEventModel;
import utils.ResMessage;

import java.time.LocalDateTime;
import java.util.Set;

public class UserServices {

    public void activity (String username) {
        UserController controller = new UserController();

        ResMessage<Set<GithubEventModel>> user = controller.getList(username);
        boolean success = user.success();

        if (success) {
            for (GithubEventModel item : controller.getList(username).data()) {
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
        } else {
            System.out.println(user.message());
        }

    }

}
