package com.tejaswi.enterpriseworkforcemanagement.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ForcePasswordChangeFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();

        //allowing public end points
        if(path.startsWith("/auth/signin")
                || path.startsWith("/auth/change-password")
                || path.startsWith("/auth/forgot-password")
                || path.startsWith("/auth/reset-password")
                || path.startsWith("/auth/refresh-token")){

            filterChain.doFilter(request,response);
            return;
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            filterChain.doFilter(request,response);
            return;
        }

        Object principal = authentication.getPrincipal();

        if(principal instanceof CustomUserDetails userDetails){

            if(!userDetails.getUser().isPasswordChanged()){

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                response.setContentType("application/json");

                response.getWriter().write("""
                {
                  "message":"Please change your temporary password first."
                }
                """);

                return;
            }
        }

        filterChain.doFilter(request,response);
    }



}
