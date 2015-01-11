package com.morgan.server.auth;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.template.soy.tofu.SoyTofu.Renderer;
import com.morgan.server.constants.PageConstantsHelper;
import com.morgan.server.util.soy.fake.FakeSoyTemplateFactory;

/**
 * Tests for the {@link AuthHostPageServlet} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthHostPageServletTest {

  @Mock private HttpServletRequest mockRequest;
  @Mock private HttpServletResponse mockResponse;

  @Mock private Renderer mockRenderer;

  @Mock private PageConstantsHelper mockPageConstantsHelper;

  private StringWriter writer;
  private PrintWriter pWriter;

  private AuthHostPageServlet servlet;

  @Before public void createTestInstances() throws Exception {
    writer = new StringWriter();
    pWriter = new PrintWriter(writer);
    when(mockResponse.getWriter()).thenReturn(pWriter);

    AuthSoyTemplate soy = FakeSoyTemplateFactory.createProxyWithRendererFor(
        mockRenderer, AuthSoyTemplate.class);
    servlet = new AuthHostPageServlet(soy, mockPageConstantsHelper);
  }

  @Test public void doGet() throws Exception {
    servlet.doGet(mockRequest, mockResponse);

    verify(mockRenderer).render(pWriter);
    verify(mockResponse).setContentType("text/html");
    verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
    verify(mockResponse).setHeader("Cache-Control", "no-cache");
    verify(mockResponse).setHeader("Pragma", "no-cache");
    verify(mockResponse).setDateHeader("Expires", 0);
  }
}
