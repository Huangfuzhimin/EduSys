package org.itheima.edu.util;


import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Poplar
 *
 * 打印请求信息
 */
public class LogUtils {
	
	
	/**
	 * 打印请求信息
	 * @param request
	 */
	public static void printRequest(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();		
		
		String requestURI = request.getRequestURL().toString();
		sb.append("------new request------"+ "\n");
		sb.append(
				"URI: " + requestURI + "\n" +
				"Method: " + request.getMethod() + " \n"
				);
		sb.append("------headers------"+ "\n");
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String headerName = headerNames.nextElement();
			sb.append(headerName + ": " + request.getHeader(headerName) + "\n");
		}
		
		sb.append("\n");

		sb.append("------params------"+ "\n");
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Entry<String, String[]> entry : parameterMap.entrySet()) {
			sb.append(entry.getKey() + ": " + entry.getValue()[0] + "\n");
		}
		System.out.println(sb.toString());
	}
	
}
