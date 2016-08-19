package security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import entities.User;
import models.JsonWebTokenDecoder;

@Component
public class RestSecurityInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	JsonWebTokenDecoder decoder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getHeader("x-access-token") != null) {
			User u = decoder.decodeUserJwt(request.getHeader("x-access-token"));
			if (u != null) {
				request.setAttribute("user", u);
				return true;
			}
		}
		response.sendRedirect("http://localhost:8080/auth/access-denied");
		return false;
	}
}
