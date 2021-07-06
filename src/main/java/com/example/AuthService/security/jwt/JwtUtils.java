package com.example.AuthService.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.AuthService.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import lombok.Data;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * Util class that generates and validates the JWT Token.
 */
@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${jwtSecret}")
  private String jwtSecret;

  @Value("${jwtExpirationMs}")
  private int jwtExpirationMs;

  
  /*
   * This method generates the JWT Token using the JJWT library. UserPrincipal is retrieved from the authentication parameter.
   * We can get the user name from that parameter and set it as a subject for the JWT Builder. The subject is something like a 
   * client(object) for witch we generate the JWT. We also set a expiration time and sign the whole JWT with HS512 (SHA-512 Hash algorithm)
  */
  public String generateJwtToken(Authentication authentication) {
	  
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  /*Returns the user name from the JWT Token.*/
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  /* Validation of the JWT Token. */
  
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}