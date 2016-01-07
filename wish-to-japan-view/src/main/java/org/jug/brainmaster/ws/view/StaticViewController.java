package org.jug.brainmaster.ws.view;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "staticViewController", urlPatterns = "/static/*")
public class StaticViewController extends HttpServlet{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/WEB-INF/pemenang-periode-1.jsp").forward(request, response);
  }
}
