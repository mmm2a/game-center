package com.morgan.server.staticres;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.morgan.server.staticres.StaticResourcesManager.ResourceSource;

/**
 * Tests for the {@link StaticResourcesHttpServlet} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticResourcesHttpServletTest {

  @Mock private HttpServletRequest mockRequest;
  @Mock private HttpServletResponse mockResponse;
  @Mock private StaticResourcesManager mockManager;
  @Mock private ResourceSource mockResourceSource;

  private StaticResourcesHttpServlet servlet;

  @Before public void createTestInstances() {
    servlet = new StaticResourcesHttpServlet(mockManager);
  }

  @Test public void doGet_invalidPathFormat_setsNotFoundStatus() throws Exception {
    when(mockRequest.getPathInfo()).thenReturn("/foo");
    servlet.doGet(mockRequest, mockResponse);
    verify(mockResponse).setStatus(HttpStatus.NOT_FOUND_404);
    verifyNoMoreInteractions(mockResponse);

    reset(mockResponse);

    when(mockRequest.getPathInfo()).thenReturn("/foo/bar/bat");
    servlet.doGet(mockRequest, mockResponse);
    verify(mockResponse).setStatus(HttpStatus.NOT_FOUND_404);
    verifyNoMoreInteractions(mockResponse);
  }

  @Test public void doGet_noSuchResource_setsNotFoundStatus() throws Exception {
    when(mockRequest.getPathInfo()).thenReturn("/foo/bar");
    servlet.doGet(mockRequest, mockResponse);
    verify(mockResponse).setStatus(HttpStatus.NOT_FOUND_404);
    verifyNoMoreInteractions(mockResponse);
  }

  @Test public void doGet() throws Exception {
    when(mockRequest.getPathInfo()).thenReturn("/foo/bar");
    when(mockManager.getResource("foo", "bar")).thenReturn(mockResourceSource);
    servlet.doGet(mockRequest, mockResponse);
    verify(mockResourceSource).doGet(mockResponse);
    verifyZeroInteractions(mockResponse);
  }
}
