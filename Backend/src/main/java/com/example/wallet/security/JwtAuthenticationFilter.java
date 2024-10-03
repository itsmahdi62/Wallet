//package com.example.wallet.security;
//
//
//import com.example.wallet.entity.Person;
//import com.example.wallet.service.JwtUtil;
//import com.example.wallet.service.PersonService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final  PersonService personService;
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private final JwtHelper jwtHelper;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//         String requestHeader = request.getHeader("Authorization");
//            System.out.println(requestHeader);
//        String userNationalId = null;
//        String jwt = null;
//
//        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
//            jwt = requestHeader.substring(7);
//            userNationalId = jwtUtil.extractUserNationalId(jwt); // Extract `userNationalId`
//        }
//
//        if (userNationalId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            Person person = personService.findByNationalId(userNationalId); // Find person by `userNationalId`
//
//            if (jwtUtil.validateToken(jwt, person)) {
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(person, null, new ArrayList<>()); // No authorities for now
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
package com.example.wallet.security;


import com.example.wallet.entity.Person;
import com.example.wallet.service.PersonService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final PersonService personService;
    private final JwtHelper jwtHelper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        logger.info(requestHeader);
        String userNationalId = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            try{
                userNationalId = this.jwtHelper.getNationalIdFromToken(token);
            }catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException e){
                logger.info("Illegal argument while fetching NationalId");
                e.printStackTrace();
            }
        }else{
            logger.warn("JWT token does not begin with Bearer string ");
        }
        if(userNationalId != null && SecurityContextHolder.getContext().getAuthentication()==null ){
            // fetch user details
            Person person = personService.findByNationalId(userNationalId); // Find person by `userNationalId`

            if (jwtHelper.validateToken(token, person)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(person, null, new ArrayList<>()); // No authorities for now
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                logger.info("Validation fails !");
            }
        }
        filterChain.doFilter(request, response);
    }
}