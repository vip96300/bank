package org.cloud.bank.client.interceptor;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.model.User;
import org.cloud.bank.client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

@Component
public class UserInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserRepository userRepository;
	
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(UserAuthor.class)) {
        	long userid=Long.parseLong(request.getHeader("userid"));
            User user=userRepository.findOne(userid);
            int level=method.getAnnotation(UserAuthor.class).level();
            if(org.springframework.util.StringUtils.isEmpty(user)){
            	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            if(user.getLevel()>level){
            	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }
}
