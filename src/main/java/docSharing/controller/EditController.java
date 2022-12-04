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
        if (message.getSender() != message.getReceiver())
        {
            String text = message.getContent();


        }
        return message;
    }


    /*  //     if (updateData.content == null && updateData.startPos < updateData.endPos) {
  //       text =
  //         text.substring(0, updateData.startPos) +
  //         text.substring(updateData.endPos, text.length);
  //     } else if (updateData.content == null) {
  //       text =
  //         text.substring(0, updateData.position + 1) +
  //         text.substring(updateData.position + 2, text.length);
  //     } else {
  //       text =
  //         text.substring(0, updateData.position) +
  //         updateData.content +
  //         text.substring(updateData.position, text.length);
  //     }
  //     textArea.val(text);
  //     if (updateData.position < start) {
  //       start++;
  //       textArea[0].setSelectionRange(start, start);
  //     }
*/

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
