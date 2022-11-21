package docSharing.Entities;

public class Contender {

    String email;
    UserRole userRole;

    public Contender() {
    }

    public Contender(String email, UserRole userRole) {
        this.email = email;
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "Contender{" +
                "email='" + email + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
