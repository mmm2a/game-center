package com.morgan.server.game;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.security.SecurityInformationHelper;
import com.morgan.server.security.UserIdObfuscator;

/**
 * Simple "hello world" servlet to prove the server is running.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class HelloWorldServlet extends HttpServlet {

  static final long serialVersionUID = 0L;

  private final SecurityInformationHelper helper;
  private final UserIdObfuscator obfuscator;

  @Inject HelloWorldServlet(SecurityInformationHelper helper, UserIdObfuscator obfuscator) {
    this.helper = helper;
    this.obfuscator = obfuscator;
  }

  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try (ServletOutputStream out = resp.getOutputStream()) {
      PrintStream pOut = new PrintStream(out);
      pOut.println("<html><body><h1>Hello, World!</h1>\n");
      pOut.println("<ul>");
      for (Map.Entry<String, String> entry : helper.getInformation().entrySet()) {
        pOut.format("<li><b>%s</b>: %s\n", entry.getKey(), entry.getValue());
      }
      pOut.println("</ul>");
      String encrypted = obfuscator.obfuscateId(7L);
      pOut.format("<b>Obfuscating 7L:</b> %s<br>\n", encrypted);
      pOut.format("<b>Deobfuscating:</b> %d<br>\n", obfuscator.deobfuscateId(encrypted));
      pOut.println("</body></html>");
      pOut.flush();
    }

    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
