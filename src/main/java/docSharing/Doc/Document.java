package docSharing.Doc;

import docSharing.Entities.User;
import docSharing.Entities.UserRole;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Document {
    Map<User, UserRole> UsersInDoc;
    String Header;
    File FileText;
    String Path;

    public Document() {
        this.UsersInDoc = new HashMap<>();
    }

    public Document(Map<User, UserRole> usersInDoc, String header, File fileText, String path) {
        UsersInDoc = usersInDoc;
        Header = header;
        FileText = fileText;
        Path = path;
    }



    public Map<User, UserRole> getUsersInDoc() {
        return UsersInDoc;
    }

    public void setUsersInDoc(Map<User, UserRole> usersInDoc) {
        UsersInDoc = usersInDoc;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public File getFileText() {
        return FileText;
    }

    public void setFileText(File fileText) {
        FileText = fileText;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }
}
