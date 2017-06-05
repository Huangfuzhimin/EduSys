package org.itheima.edu.interceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.itheima.edu.common.Constants;
import org.itheima.edu.dao.UserEntityDao;
import org.itheima.edu.entity.UserEntity;
import org.itheima.edu.util.ResponseCode;
import org.itheima.edu.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	UserEntityDao userDao;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

//		LogUtils.printRequest(request);

		String accept = request.getHeader("Accept");
		String token = request.getHeader(Constants.HEADER_TOKEN);
//		if (accept != null && accept.contains("json")) {
			// json结果
			ServletOutputStream os = response.getOutputStream();

			if (StringUtils.isEmpty(token)) {
				String error = ResponseUtils.error(ResponseCode.Validate.TOKEN_EMPTY, "缺失必要的头t");
				os.write(error.getBytes("UTF-8"));
				return false;
			}

			UserEntity entity = userDao.findByToken(token);
			if (entity == null) {
				String error = ResponseUtils.error(ResponseCode.Validate.USER_NOT_EXIST, "用户不存在或已过时需要重新登录");
				os.write(error.getBytes("UTF-8"));
				return false;
			}
//		} else {
//			// html结果
//		}
		return true;
	}

}
