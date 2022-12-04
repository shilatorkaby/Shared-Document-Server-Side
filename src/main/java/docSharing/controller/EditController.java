package docSharing.controller;

import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.Entities.UserBody;
import docSharing.service.AuthService;
import docSharing.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EditController {

    @Autowired
    AuthService authService;
    @Autowired
    DocService docService;

    @MessageMapping("/join")
    public void sendPlainMessage(JoinMessage message) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@\n\n\n");
        System.out.println(message.user + " joined");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@\n\n\n");
    }


    @MessageMapping("/update")
    @SendTo("/topic/updates")
    public UpdateMessage sendPlainMessage(UpdateMessage message) {

        UserBody user = authService.getCachedUser(message.user);

        if (user != null) {
            Document document = docService.getDocumentById(user, message.docId);
            if (document != null) {
                String text = document.getFileContent();

                System.out.println("Before message: " + message + "\n\n");

                if (message.content == null && message.startPos < message.endPos) {
                    text = text.substring(0, message.startPos) + text.substring(message.endPos, text.length());
                } else if (message.content == null) {
                    text = text.substring(0, message.position + 1) + text.substring(message.position + 2, text.length());
                } else {
                    text = text.substring(0, message.position) + message.content + text.substring(message.position, text.length());
                }
                document.setFileContent(text);
                docService.save(document);
            }
        }

        return message;
    }

    static class UpdateMessage {
        private String user;
        private UpdateType type;
        private String content;
        private Integer position;
        private Integer startPos;
        private Integer endPos;
        private Long docId;

        public int getStartPos() {
            return startPos;
        }

        public void setStartPos(Integer startPos) {
            this.startPos = startPos;
        }

        public Integer getEndPos() {
            return endPos;
        }

        public void setEndPos(Integer endPos) {
            this.endPos = endPos;
        }

        public UpdateMessage() {
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public UpdateType getType() {
            return type;
        }

        public void setType(UpdateType type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public Long getDocId() {
            return docId;
        }

        public void setDocId(Long docId) {
            this.docId = docId;
        }

        @Override
        public String toString() {
            return "UpdateMessage{" +
                    "user='" + user + '\'' +
                    ", type=" + type +
                    ", content='" + content + '\'' +
                    ", position=" + position +
                    ", startPos=" + startPos +
                    ", endPos=" + endPos +
                    ", docId=" + docId +
                    '}';
        }
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