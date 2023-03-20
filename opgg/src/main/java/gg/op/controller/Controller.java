package gg.op.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
	
	@GetMapping("/")
	public String main(Model model) {
		
		model.addAttribute("title", "OPGG");
		
		return "user/main";
		
	}
}
