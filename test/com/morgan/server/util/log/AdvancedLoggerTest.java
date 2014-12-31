package com.morgan.server.util.log;

import static org.mockito.Mockito.verify;

import java.io.PrintWriter;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for the {@link AdvancedLogger} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AdvancedLoggerTest {

  @Mock private MockLogger mockLogger;

  private TestableAdvancedLogger logger;

  @Before public void createTestInstances() {
    logger = new TestableAdvancedLogger();
  }

  @Test public void trace_withoutException() {
    logger.trace("format", "a", 7);
    verify(mockLogger).doLog(Level.FINEST, null, "format", "a", 7);
  }

  @Test public void trace_withException() {
    NullPointerException npe = new NullPointerException("some message");
    logger.trace(npe, "format", "a", 7);
    verify(mockLogger).doLog(Level.FINEST, npe, "format", "a", 7);
  }

  @Test public void logWriter() {
    NullPointerException npe = new NullPointerException("some message");
    try (PrintWriter writer = logger.logWriter(npe, Level.WARNING)) {
      writer.println("first line");
      writer.println("second line");
    }
    verify(mockLogger).doLog(Level.WARNING, npe, "first line\nsecond line\n");
  }

  interface MockLogger {
    void doLog(Level level, @Nullable Throwable e, String fmt, Object... args);
  }

  private class TestableAdvancedLogger extends AdvancedLogger {

    private TestableAdvancedLogger() {
      super(AdvancedLoggerTest.class);
    }

    @Override void doLog(Level level, @Nullable Throwable e, String fmt, Object... args) {
      mockLogger.doLog(level, e, fmt, args);
    }
  }
}
