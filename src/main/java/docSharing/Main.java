package docSharing;

import docSharing.Entities.User;
import docSharing.controller.UserController;

public class Main {
    public static void main(String[] args) {
        User user = new User(1234L,"shilat","shilatprojects@gmail.com","shilat1");
        UserController userController = new UserController();
       // userController.createNewUser(user);

    }

}
