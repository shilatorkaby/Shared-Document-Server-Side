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
}
