package com.ania.cookbook.application.properties;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import org.springframework.lang.NonNull;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collections;

public class JwtFilter {
//extends OncePerRequestFilter
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = authHeader.substring(7);
//        try {
//            Claims claims = JwtUtil.parseToken(token);
//            String username = claims.getSubject();
//            if (username != null) {
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (JwtException e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//            return;
//        }
//        filterChain.doFilter(request, response);
//    }
}
