package com.example.DataService.AuthService.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.DataService.AuthService.security.services.UserDetailsServiceImpl;

/*
 * Every request passes trough this filter. This is set up in WebSecurityConfig.
 */
public class AuthTokenFilter extends OncePerRequestFilter{

	 @Autowired
	  private JwtUtils jwtUtils;

	  @Autowired
	  private UserDetailsServiceImpl userDetailsService;

	  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	  
	  @Override
	  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
	    try {
	      String jwt = parseJwt(request);

	      /* We must validate the JWT before going any further.*/
	      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
	        String username = jwtUtils.getUserNameFromJwtToken(jwt);
	        /*If the user name is not null and the authentication object is null meaning we don't have an authenticated user
	         * we can proceed further. 
	        */
	        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        	/*User details provides core details of the user. It has a getAuthorities() method that returns all granted authorities.*/
	        	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	        	
		        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
		            userDetails.getAuthorities());
		        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		        SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	        
	      }
	    } catch (Exception e) {
	      logger.error("Cannot set user authentication: {}", e);
	    }

	    filterChain.doFilter(request, response);
	  }

	  /*
	   * This method looks for an Authorization header in the request. If it finds it and if the header starts 
	   * with "Bearer " it returns the substring of the header starting from the 7th character and going to the end of the header.
	   * This will return the whole JWT Token.
	   */
	  private String parseJwt(HttpServletRequest request) {
	    String headerAuth = request.getHeader("Authorization");

	    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
	      return headerAuth.substring(7, headerAuth.length());
	    }
	    return null;
	  }

}
