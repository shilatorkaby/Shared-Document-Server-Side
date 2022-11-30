package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.service.AuthService;
import docSharing.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import org.springframework.messaging.handler.annotation.SendTo;
@RestController
@CrossOrigin
@RequestMapping("/doc")
public class DocController {
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

        private String docId;

        public String getDocId() {
            return docId;
        }

        public String getStartPos() {
            return startPos;
        }
        public void setDocId(String docId) {
            this.docId = docId;
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

    @Autowired
    private DocService docService;

    @Autowired
    private AuthService authService;

    private static final Gson gson = new Gson();

        @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody Document document) {
        return docService.save(document);
    }

    @RequestMapping(value = "/fetch", method = RequestMethod.POST)
    public ResponseEntity<String> getDocumentById(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {
        User user = authService.getCachedUser(token);

        System.out.println(map.get("id"));

        if (user != null) {
            Document temp = docService.getDocumentById(user, Long.parseLong(map.get("id")));
            if (temp != null) {
                return ResponseEntity.ok(gson.toJson(temp));
            }
        }
        return ResponseEntity.notFound().build();
    }
}