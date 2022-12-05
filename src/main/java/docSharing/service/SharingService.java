package docSharing.service;

import docSharing.Entities.*;
import docSharing.repository.*;
import docSharing.utils.Email;
import docSharing.utils.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SharingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private DocPermissionRepository docPermissionRepository;

    @Autowired
    private DocumentLinkRepository documentLinkRepository;

    @Autowired
    private ContenderRepository contenderRepository;

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LogManager.getLogger(SharingService.class.getName());


    /**
     * shares the document via email to the desired contender,
     * checks if the given email is registered user, and if the invitation is not forged
     *
     * @param contender (docId, email and token type Contender)
     * @return contender, if something went wrong it returns null
     */
    public Contender shareViaEmail(Contender contender) {
        logger.info("Share via email to contender: " + contender.getEmail());
        if (!isEmailInDatabase(contender.getEmail()) || isEmailInDocument(contender.getDocId(), contender.getEmail())) {
            logger.warn("Share via email failed.");
            return null;
        }

        contender.setToken(Token.generate());
        sendmail(contender);
        contenderRepository.save(contender);
        logger.info("Share via email completed");
        return contender;
    }

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    public DocumentLink shareViaLink(String email, HashMap<String, String> map) {
        logger.info("Share via link to email: " + email);
        Long documentId = Long.parseLong(map.get("documentId"));

        DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(documentId, email);

        if (docPermission != null && docPermission.getRole() != UserRole.VIEWER) {
            logger.info("Share via link completed");
            return documentLinkRepository.findByDocId(Long.parseLong(map.get("documentId")));
        }
        logger.warn("Share via link failed.");
        return null;
    }

    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    boolean isEmailInDocument(Long id, String email) {
        return (docPermissionRepository.findByDocIdAndEmail(id, email) != null);
    }


    public void sendmail(Contender contender) {
        logger.info("Send mail to contender: " + contender.getEmail());
        String destination = contender.getEmail();
        String title = "Please accept the invitation";
        String txt = "Please click the link below to join a shared document:\n"
                + "http://localhost:8080/accept/email-invite/" + contender.getToken()
                + "\nThank you.";

        Email email = new Email.Builder().subject(title).to(destination).content(txt).build();
        logger.info("Send mail completed");
        mailSender.send(email.convertIntoMessage());
    }

    public String verifyEmailToken(String token) {
        logger.info("Verify email token :" + token);
        Contender contender = contenderRepository.findByToken(token);

        // we check if the user has a role already in the document
        if (docPermissionRepository.findByDocIdAndEmail(contender.getDocId(), contender.getEmail()) != null) {
            contenderRepository.delete(contender);
            docPermissionRepository.updatePermission(contender.getDocId(), contender.getEmail(), contender.getUserRole());
            logger.info("Verify email token completed");
            return "<h1>User successfully updated role</h1>";
        }

        User user = userRepository.findByEmail(contender.getEmail());

        if (user != null) {
            Long fId = directoryRepository.findByFatherId(user.getId() * -1).get(0).getId();
            String name = docRepository.findByDocId(contender.getDocId()).getFileName();
            directoryRepository.save(new Directory(fId, name, contender.getDocId()));

            contenderRepository.delete(contender);
            docPermissionRepository.save(new DocPermission(contender.getDocId(), contender.getEmail(), contender.getUserRole()));
            logger.info("User successfully joined document");
            return "<h1>User successfully joined document</h1>";
        }
        logger.warn("verify Email Token failed.");
        return "User couldn't join document";
    }

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    public String verifyLinkToken(String email, String token) {
        logger.info("verify Link Token: " + token);
        DocumentLink optionalDocumentLink = documentLinkRepository.findByEditorToken(token);

        if (optionalDocumentLink != null) {

            DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(optionalDocumentLink.getDocumentId(), email);

            if (docPermission == null) {
                logger.info("docPermission is null");
                docPermission = new DocPermission(optionalDocumentLink.getDocumentId(), email, UserRole.EDITOR);
                docPermissionRepository.save(docPermission);
                logger.info("User successfully added to document as editor");
                return "<h1>User successfully added to document as editor</h1>";
            }

            if (docPermission.getRole() == UserRole.VIEWER) {
                docPermissionRepository.updatePermission(optionalDocumentLink.getDocumentId(), email, UserRole.EDITOR);
                logger.info("User successfully updated to editor");
                return "<h1>User successfully updated to editor</h1>";
            }
        }

        optionalDocumentLink = documentLinkRepository.findByViewerToken(token);

        if (optionalDocumentLink != null) {

            DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(optionalDocumentLink.getDocumentId(), email);

            if (docPermission == null) {
                logger.info("docPermission is null");
                docPermission = new DocPermission(optionalDocumentLink.getDocumentId(), email, UserRole.VIEWER);
                docPermissionRepository.save(docPermission);
                logger.info("User successfully added to document as viewer");
                return "<h1>User successfully added to document as viewer</h1>";
            }
//
//            if (docPermission.getRole() != UserRole.VIEWER) {
//                docPermissionRepository.updatePermission(optionalDocumentLink.getDocumentId(), email, UserRole.EDITOR);
//                return "<h1>User successfully updated to editor</h1>";
//            }
        }
        logger.warn("User couldn't join document");
        return "User couldn't join document";
    }
}
