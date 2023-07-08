package com.mountblue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.mountblue.model.User;
import com.mountblue.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public String saveUser(@ModelAttribute User user) {
		if (userService.saveUser(user)) {
			return "login";
		} else {
			return "register";
		}
	}
}
