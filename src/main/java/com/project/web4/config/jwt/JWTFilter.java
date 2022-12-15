package com.project.web4.config.jwt;

import com.project.web4.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
@Log
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    @Autowired
    private final JWTProvider jwtProvider;
    @Autowired
    private final UserService UserService;

    @Override
    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = getTokenFromRequest(servletRequest);
            if (token != null && jwtProvider.validateToken(token)) {
                String username = jwtProvider.getLoginFromToken(token);
                UserDetails customUserDetails = UserService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(servletRequest));
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("User authentication done");
            }
        } catch (Exception e) {
            log.severe("Failed user authentication: " + e.getMessage());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        log.severe("No token found");
        return null;
    }
}