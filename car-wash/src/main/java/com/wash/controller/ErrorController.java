package com.wash.controller;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

@Clear
public class ErrorController extends Controller{
	
	private static Log log = Log.getLog(ErrorController.class);
	
	public void black(){
		render("black.html");
	}
}
