package com.oracle.vikings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/LogAnalyzer")
	public String welcome(Model model) {
		System.out.println("Welcome to logAnalyzer");
		model.addAttribute("message","Welcome to logAnalyzer!");
		return "index";
	}
	
	@GetMapping("/LogAnalyzer/home")
	public String showHome() {
		return "index";
	}
	
	@GetMapping("/LogAnalyzer/showMinifyOdl")
	public String showMinifyOdl() {
		return "minifyOdl";
	}
	
	@GetMapping("/LogAnalyzer/showPlSql")
	public String showPlSql() {
		return "plsql";
	}
}
