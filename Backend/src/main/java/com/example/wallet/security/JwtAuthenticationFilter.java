
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
        // logger.info(requestHeader);
        String userNationalId = null;
        String token = null;

        String requestUri = request.getRequestURI();

        // Skip filtering for signup route or other unauthenticated routes
        if (requestUri.equals("/api/v1/person/signup")) {
            filterChain.doFilter(request, response);
            return;  // Skip JWT authentication for this route
        }


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
            // fetch person details
            logger.info("ddd");
            Person person = personService.findByNationalId(userNationalId); // Find person by `userNationalId`
            if (jwtHelper.validateToken(token, person)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(person, null, new ArrayList<>()); // No authorities for now
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.warn(SecurityContextHolder.getContext().getAuthentication());
                logger.warn(SecurityContextHolder.getContext());

            }else{
                logger.warn("Token validation failed for person: " + userNationalId);
            }
        }
        filterChain.doFilter(request, response);
    }
}