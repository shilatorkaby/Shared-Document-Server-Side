package docSharing.service;

import docSharing.Entities.*;
import docSharing.repository.ContenderRepository;
import docSharing.repository.DocPermissionRepository;
import docSharing.repository.DocumentLinkRepository;
import docSharing.repository.UserRepository;
import docSharing.utils.Email;
import docSharing.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SharingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocPermissionRepository docPermissionRepository;

    @Autowired
    private DocumentLinkRepository documentLinkRepository;

    @Autowired
    private ContenderRepository contenderRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Contender shareViaEmail(Contender contender) {

        if (!isEmailInDatabase(contender.getEmail())) {
            return null;
        }

        contender.setToken(Token.generate());
        sendmail(contender);
        contenderRepository.save(contender);

        return contender;
    }

    public DocumentLink shareViaLink(String email, HashMap<String, String> map) {

        Long documentId = Long.parseLong(map.get("documentId"));

        DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(documentId, email);

        if (docPermission != null && docPermission.getRole() != UserRole.VIEWER) {
            return documentLinkRepository.findByDocId(Long.parseLong(map.get("documentId")));
        }

        return null;
    }

    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    boolean isEmailInDocument(Long id, String email) {
        return (docPermissionRepository.findByDocIdAndEmail(id, email) != null);
    }


    public void sendmail(Contender contender) {
        String destination = contender.getEmail();
        String title = "Please accept the invitation";
        String txt = "Please click the link below to join a shared document:\n"
                + "http://localhost:8080/accept/email-invite/" + contender.getToken()
                + "\nThank you.";

        Email email = new Email.Builder().subject(title).to(destination).content(txt).build();
        mailSender.send(email.convertIntoMessage());
    }

    public String verifyEmailToken(String token) {

        Contender contender = contenderRepository.findByToken(token);

        // we check if the user has a role already in the document
        if (docPermissionRepository.findByDocIdAndEmail(contender.getDocId(), contender.getEmail()) != null) {
            contenderRepository.delete(contender);
            docPermissionRepository.updatePermission(contender.getDocId(), contender.getEmail(), contender.getUserRole());
            return "<h1>User successfully updated role</h1>";
        }

        // we check if the contender exists
        if (contender != null) {
            contenderRepository.delete(contender);
            docPermissionRepository.save(new DocPermission(contender.getDocId(), contender.getEmail(), contender.getUserRole()));
            return "<h1>User successfully joined document</h1>";
        }

        return "User couldn't join document";
    }

    public String verifyLinkToken(String email, String token) {

        DocumentLink optionalDocumentLink = documentLinkRepository.findByEditorToken(token);

        if (optionalDocumentLink != null) {

            DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(optionalDocumentLink.getDocumentId(), email);

            if (docPermission == null) {
                docPermission = new DocPermission(optionalDocumentLink.getDocumentId(), email, UserRole.EDITOR);
                docPermissionRepository.save(docPermission);
                return "<h1>User successfully added to document as editor</h1>";
            }

            if (docPermission.getRole() == UserRole.VIEWER) {
                docPermissionRepository.updatePermission(optionalDocumentLink.getDocumentId(), email, UserRole.EDITOR);
                return "<h1>User successfully updated to editor</h1>";
            }
        }

        optionalDocumentLink = documentLinkRepository.findByViewerToken(token);

        if (optionalDocumentLink != null) {

            DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(optionalDocumentLink.getDocumentId(), email);

            if (docPermission == null) {
                docPermission = new DocPermission(optionalDocumentLink.getDocumentId(), email, UserRole.VIEWER);
                docPermissionRepository.save(docPermission);
                return "<h1>User successfully added to document as viewer</h1>";
            }
//
//            if (docPermission.getRole() != UserRole.VIEWER) {
//                docPermissionRepository.updatePermission(optionalDocumentLink.getDocumentId(), email, UserRole.EDITOR);
//                return "<h1>User successfully updated to editor</h1>";
//            }
        }
        return "User couldn't join document";
    }
}
