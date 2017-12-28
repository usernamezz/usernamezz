package com.example.controller;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ForumController {
    @Autowired
    private UserService userService;

    @Qualifier("roleRepository")
    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(value={"/", "/forum"}, method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", user);
        if (user != null){
            modelAndView.addObject("isAdmin", userService.isUserHasRole(user, "ADMIN"));
        }
        modelAndView.setViewName("forum");
        return modelAndView;
    }

    @RequestMapping(value="/admin/forum", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("admin/forum");
        return modelAndView;
    }

    @RequestMapping("/default")
    public String defaultAfterLogin() {
        if (userCheck("ADMIN")) {
            return "redirect:/admin/forum";
        }
        return "redirect:/forum";
    }

    private Boolean userCheck(Role role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        return userCheck(user, role);
    }

    private Boolean userCheck(String userRole){
        Role role = roleRepository.findByRole(userRole);
        return userCheck(role);
    }

    private Boolean userCheck(User user, Role role) {
        return (!(null == user) && user.getRoles().contains(role));
    }

    private Boolean userCheck(User user, String userRole) {
        Role role = roleRepository.findByRole(userRole);
        return userCheck(user, role);
    }
}
