package cn.itcast.bos.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MyFilter extends org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter{

	
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		
		String url = request.getServletPath();
		
		if(url.endsWith("/services")) {
			chain.doFilter(req, res);
		}else {
			super.doFilter(req, res, chain);
		}
		
		
	}
	
}
