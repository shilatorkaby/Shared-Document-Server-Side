package docSharing.Entities;

import docSharing.utils.Token;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DocumentLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long documentId;

    private String viewerToken;

    private String editorToken;

    public DocumentLink() {
    }

    public DocumentLink(Long documentId) {
        this.documentId = documentId;
        this.viewerToken = Token.generate();
        this.editorToken = Token.generate();
    }

    public Long getDocumentId() {
        return documentId;
    }

    public String getViewerToken() {
        return viewerToken;
    }

    public String getEditorToken() {
        return editorToken;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public void setViewerToken(String viewerToken) {
        this.viewerToken = viewerToken;
    }

    public void setEditorToken(String editorToken) {
        this.editorToken = editorToken;
    }
}

