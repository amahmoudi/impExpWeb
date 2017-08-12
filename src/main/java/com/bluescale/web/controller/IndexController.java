package com.bluescale.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bluescale.business.model.User;
import com.bluescale.business.service.UserService;

@Controller
public class IndexController {
	 @Resource(name="userService")
	    UserService  userService;
	    @Autowired
	    private Environment environment;
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String getIndexPage(ModelMap model) {
    	 String fileName = environment.getRequiredProperty("file.name");
    	User findUserFromSession = userService.findUserFromSession();
		String nameUser = findUserFromSession.getUserEmail();
		model.addAttribute("currentUserName",nameUser);
    	model.addAttribute("fileName",fileName);
    	if(findUserFromSession.getAdminEnabled()){
    		return "admin";
    	}
    	return "loadFileView";
    }
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
    
    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		String nameUser = userService.findUserFromSession().getUserEmail();
		model.addAttribute("currentUserName", nameUser);
		return "accessDenied";
	}
	
}
