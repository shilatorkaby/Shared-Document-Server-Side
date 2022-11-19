package docSharing.controller;

import docSharing.Entities.User;
import docSharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class AppController {
    @Autowired
    private UserService service;

    @RequestMapping(value ="/process_register" ,method = RequestMethod.POST)
    public String processRegister(User user, HttpServletRequest request)
    {
        service.register(user,getSiteURL(request));
        return "register succeed";
    }

    public String getSiteURL(HttpServletRequest request)
    {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(),"");
        //returns the real context path of the web application
        //the verification link will work when the user opens the email.
    }

    @RequestMapping(value ="/verify" ,method = RequestMethod.GET)
    public String verifyUser(@Param("code") String code) {
        if (service.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @RequestMapping(value ="/register" ,method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }






}
