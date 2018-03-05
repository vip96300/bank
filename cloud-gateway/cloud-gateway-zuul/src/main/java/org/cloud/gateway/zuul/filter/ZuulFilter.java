package org.cloud.gateway.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.context.RequestContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ZuulFilter extends com.netflix.zuul.ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(ZuulFilter.class);
	/**
	 * 密钥
	 */
	public static final String SECRET = "huanghongfei";

    /**
     * 过滤类型
     * pre: 路由之间
     * routing: 路由之时
     * post: 路由之后
     * error: 发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 写判断逻辑，是否要过滤，true永远过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 具体业务逻辑
     * @return
     */
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        logger.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        if (request.getRequestURI().contains("login")) {
            return null;
        }
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(401);
        }
        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        String userid=claims.getBody().get("userid").toString();
        context.addZuulRequestHeader("userid",userid);
        return null;
    }
}
