package docSharing.controller;

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
        public UpdateMessage sendPlainMessage(UpdateMessage message) {
            return message;
        }

        static class UpdateMessage {
            private String user;
            private UpdateType type;
            private String content;
            private int position;
            private String startPos;
            private String endPos;



            public String getStartPos() {
                return startPos;
            }

            public void setStartPos(String startPos) {
                this.startPos = startPos;
            }

            public String getEndPos() {
                return endPos;
            }

            public void setEndPos(String endPos) {
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
        }

        public enum UpdateType{
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
