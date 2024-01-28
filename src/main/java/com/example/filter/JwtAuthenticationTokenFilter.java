package com.example.filter;


import com.example.dto.authentication.LoginUser;
import com.example.dto.authentication.LoginUserRedis;
import com.example.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String REDIS_PRE = "token:";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //request.getRequestURI()
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            token=request.getParameter("token");
        }
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String uuid = JWTUtil.getUserIdFromToken(token);
        if(Objects.isNull(uuid)){
            filterChain.doFilter(request, response);
            return;
        }
        //从redis中获取用户信息
        ValueOperations<String, LoginUserRedis> operations=redisTemplate.opsForValue();
        LoginUserRedis loginUserRedis=operations.get(REDIS_PRE+uuid);
        if(loginUserRedis==null){
            filterChain.doFilter(request, response);
            return;
        }
        LoginUser user=loginUserRedis.getLoginUser();
        if(Objects.isNull(user)){
            filterChain.doFilter(request, response);
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
