package com.ilanherbach.javaspringboottodo.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ilanherbach.javaspringboottodo.user.IUserRepository;
import com.ilanherbach.javaspringboottodo.user.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// generic anotation so spring can manage
@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String servletPath = request.getServletPath();

    if (servletPath.equals("/tasks")) {

      // handle and validate authentication
      String auth = request.getHeader("Authorization");
      String authEncoded = auth.substring("Basic".length()).trim();
      byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
      String authString = new String(authDecoded);
      String[] authSplitted = authString.split(":");
      UserModel user = this.userRepository.findByUsername(authSplitted[0]);

      if (user == null) {
        response.sendError(401);
        return;
      }

      byte[] hashedAuth = user.getPassword().getBytes();

      BCrypt.Result passwordMatched = BCrypt.verifyer().verify(authSplitted[1].getBytes(), hashedAuth);

      if (passwordMatched.verified) {
        request.setAttribute("idUser", user.getId());
        filterChain.doFilter(request, response);
        return;
      }

      response.sendError(401);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
