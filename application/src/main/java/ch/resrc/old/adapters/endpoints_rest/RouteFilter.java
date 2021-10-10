package ch.resrc.old.adapters.endpoints_rest;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.regex.*;

@WebFilter(urlPatterns = "/*")
public class RouteFilter extends HttpFilter {

  private static final Pattern FILE_NAME_PATTERN = Pattern.compile(".*[.][a-zA-Z\\d]+");

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    chain.doFilter(request, response);

    if (response.getStatus() == 404) {
      String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
      if (!FILE_NAME_PATTERN.matcher(path).matches()) {
        response.setStatus(200);
        request.getRequestDispatcher("/").forward(request, response);
      }
    }
  }
}
