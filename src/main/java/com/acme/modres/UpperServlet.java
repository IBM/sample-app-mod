package com.acme.modres;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/resorts/upper")
public class UpperServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    String originalStr = request.getParameter("input");
    if (originalStr == null) {
      originalStr = "";
    }

    String newStr = originalStr.toUpperCase();
    newStr = escapeHTML(newStr);

    PrintWriter out = response.getWriter();
    out.print("<br/><b>capitalized input " + newStr + "</b>");
  }

  public String escapeHTML(String input) {
    if (input == null) {
      return null;
    }

    // Replace special characters with HTML entities
    String escapedInput = input
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")
        .replace("(", "&#40;")
        .replace(")", "&#41;")
        .replace("+", "&#43;")
        .replace("%", "&#37;")
        .replace(";", "&#59;");

    return escapedInput;
  }
}
