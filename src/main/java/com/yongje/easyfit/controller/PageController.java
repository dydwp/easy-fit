package com.yongje.easyfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/guide")
	public String guide() {
		return "guide";
	}
	
	@GetMapping("/calendar")
	public String calendar() {
		return "calendar";
	}
	
	@GetMapping("/login")
	public String login() {
	    return "login";
	}
}
