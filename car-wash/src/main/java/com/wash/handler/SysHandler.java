package com.wash.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfinal.handler.Handler;

public class SysHandler extends Handler{
	
	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {

		String CPATH = request.getContextPath();
		request.setAttribute("CPATH", CPATH);
		request.setAttribute("SPATH", CPATH + "/static");
		next.handle(target, request, response, isHandled);

	}
}
