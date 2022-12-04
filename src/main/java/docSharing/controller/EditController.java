package docSharing.controller;

import docSharing.Entities.UpdatedMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EditController {

    @MessageMapping("/join")
    public void sendPlainMessage(JoinMessage message) {
        System.out.println(message.user + " joined");
    }


    @MessageMapping("/update")
    @SendTo("/topic/updates")
    public UpdatedMessage sendPlainMessage(UpdatedMessage message) {
        System.out.println(message.getContent());
        return message;
    }

    public enum UpdateType {
        DELETE,
        APPEND,
        DELETE_RANGE,
        APPEND_RANGE
    }

    private class JoinMessage {
        private String user;

        public JoinMessage() {
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
