package docSharing.Entities;

import docSharing.controller.EditController;

public class UpdatedMessage {
    private String sender;
    private String receiver;
    private String receiverDocId;
    private String senderDocId;

    private EditController.UpdateType type;
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

    public UpdatedMessage() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public EditController.UpdateType getType() {
        return type;
    }

    public void setType(EditController.UpdateType type) {
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

    public String getReceiverDocId() {
        return receiverDocId;
    }

    public void setReceiverDocId(String receiverDocId) {
        this.receiverDocId = receiverDocId;
    }

    public String getSenderDocId() {
        return senderDocId;
    }

    public void setSenderDocId(String senderDocId) {
        this.senderDocId = senderDocId;
    }
}
