package com.morgan.server.polymer;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Function;
import com.google.common.net.MediaType;

/**
 * Tests for the {@link PolymerComponentServlet} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class PolymerComponentServletTest {

  private static final Function<String, MediaType> MAPPER =
      new Function<String, MediaType>() {
        @Override @Nullable public MediaType apply(@Nullable String input) {
          if ("test-resources.txt".equals(input)) {
            return MediaType.PLAIN_TEXT_UTF_8;
          }

          return MediaType.OCTET_STREAM;
        }
      };

  @Mock private HttpServletRequest mockReq;
  @Mock private HttpServletResponse mockResp;

  private PolymerComponentServlet servlet;
  private ServletOutputStream sout;
  private ByteArrayOutputStream out;

  @Before public void createTestInstances() {
    servlet = new PolymerComponentServlet(MAPPER);
  }

  @Before public void setUpCommonMockInteractions() throws Exception {
    out = new ByteArrayOutputStream();
    sout = new ServletOutputStream() {
      @Override public void write(int b) throws IOException {
        out.write(b);
      }

      @Override public void setWriteListener(WriteListener arg0) {
        // Do nothing
      }

      @Override public boolean isReady() {
        // Do nothing
        return true;
      }
    };
    when(mockResp.getOutputStream()).thenReturn(sout);

    when(mockReq.getPathInfo()).thenReturn("/test-resources.txt");
  }

  @Test public void doGet() throws Exception {
    servlet.doGet(mockReq, mockResp);
    verify(mockResp).setContentType(MediaType.PLAIN_TEXT_UTF_8.toString());
    assertThat(new String(out.toByteArray(), Charset.defaultCharset()))
        .isEqualTo("Hello, World!");
  }
}
