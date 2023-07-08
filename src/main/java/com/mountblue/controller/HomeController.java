package com.mountblue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mountblue.service.PostService;

@Controller
public class HomeController {

	@Autowired
	private PostService postService;

	@GetMapping("/")
	public String home(Model model,
					   @RequestParam(value="order", defaultValue="") String order,
					   @RequestParam(value="search", defaultValue="") String search,
					   @RequestParam(value="author", defaultValue="") String author,
					   @RequestParam(value="dateTime", defaultValue="") String dateTime,
					   @RequestParam(value="tag", defaultValue="") String tag,
					   @RequestParam(value = "start", defaultValue = "0", required = false) int pageNumber,
					   @RequestParam(value = "limit", defaultValue = "10", required = false) int pageSize) {
		pageNumber/=10;
		postService.searchFilterSort(order, search, author, dateTime, tag, pageNumber, pageSize, model);
		model.addAttribute("order", order);
		model.addAttribute("search", search);
		model.addAttribute("author", author);
		model.addAttribute("dateTime", dateTime);
		model.addAttribute("tag", tag);
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}
}
