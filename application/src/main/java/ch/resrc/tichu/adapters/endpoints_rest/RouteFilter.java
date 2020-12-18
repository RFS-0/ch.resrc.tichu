package ch.resrc.tichu.adapters.endpoints_rest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

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