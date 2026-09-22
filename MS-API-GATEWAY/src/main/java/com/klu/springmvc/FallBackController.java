package com.klu.springmvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {
	@GetMapping("/fallback/ms1")
	public String ms1Fallback() {
		return "MS1 service down ... try later!";
	}
	
	@GetMapping("/fallback/ms2")
	public String ms2Fallback() {
		return "MS@ service down ... try later!";
	}
}

