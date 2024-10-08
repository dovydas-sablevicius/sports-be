package com.sourcery.sport.security.filter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Profile("!test")
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private static final String HEADER = "Authorization";
  private static final String PREFIX = "Bearer ";
  private final String authDomain;
  private final JwkProvider jwkProvider;

  public JWTAuthenticationFilter(
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String authDomain) {
    this.authDomain = authDomain;
    this.jwkProvider = createJwkProvider(authDomain);
  }

  private JwkProvider createJwkProvider(String authDomain) {
    try {
      String jwksUrl = authDomain + ".well-known/jwks.json";
      return new JwkProviderBuilder(new URL(jwksUrl))
          .cached(1, Duration.ofHours(24))
          .build();
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid issuer-uri provided", e);
    }
  }

  // This function is called by the SecurityFilterChain chain when HTTP request is received.
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain
  ) throws ServletException, IOException {
    try {
      if (checkJWTToken(request)) {
        DecodedJWT decodedJWT = validateToken(request);

        setUpSpringAuthentication(decodedJWT);
      } else {
        SecurityContextHolder.clearContext();
      }
      chain.doFilter(request, response);
    } catch (JwkException | JWTVerificationException e) {
      logger.error(e);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
    }
  }

  // Checks if the incoming HTTP request contains a JWT token in the header
  private boolean checkJWTToken(HttpServletRequest request) {
    String authenticationHeader = request.getHeader(HEADER);
    return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
  }

  // This function is responsible for decoding and validating the JWT token.
  private DecodedJWT validateToken(HttpServletRequest request) throws JwkException {
    String token = request.getHeader(HEADER).replace(PREFIX, "");
    DecodedJWT jwt = JWT.decode(token);
    Jwk jwk = jwkProvider.get(jwt.getKeyId());
    Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

    JWTVerifier verifier = JWT.require(algorithm)
        .withIssuer(authDomain)
        .acceptLeeway(30) // 30 seconds of leeway
        .build();

    return verifier.verify(token);
  }

  // Extracts the user's authentication information from the JWT.
  // UsernamePasswordAuthenticationToken object is then set in the
  // SecurityContextHolder object for use by the rest of the application.
  private void setUpSpringAuthentication(DecodedJWT decodedJWT) {
    String subject = decodedJWT.getSubject();
    //Extract user roles from the decoded JWT
    List<String> roles = decodedJWT.getClaim("https://sourcery.sports.com/roles").asList(String.class);
    // Convert roles to GrantedAuthority objects
    List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        subject,
        null,
        authorities
    );
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
