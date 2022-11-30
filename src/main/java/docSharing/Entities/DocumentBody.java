package docSharing.Entities;


public class DocumentBody {
    private String email;
    private String fileName;
    private Long fatherId;

    public DocumentBody() {
    }

    public DocumentBody(Long fatherId, String fileName,String email) {
        this.email = email;
        this.fileName = fileName;
        this.fatherId = fatherId;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }
}
