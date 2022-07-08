package com.parking.config;
 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.parking.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
 
@Component
public class CustomPageHandler implements AuthenticationSuccessHandler {
	String redirectUrl = null;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    		throws IOException, ServletException {
        
 
        String role = authentication.getAuthorities().toString();
        System.out.println(role);
        if(role.contains("ROLE_ADMIN")){
            redirectUrl = "/admin/index";
        } else if(role.contains("ROLE_USER")) {
            redirectUrl = "/user/index";
        }
        System.out.println("redirectUrl " + redirectUrl);
        if (redirectUrl == null) {
            throw new IllegalStateException();
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}