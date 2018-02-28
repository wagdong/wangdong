package com.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 汪冬
 * @Date 2018/2/28
 */
@RestController
public class BaseController {
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
}
