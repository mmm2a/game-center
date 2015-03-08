package com.morgan.server.util.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

/**
 * A type of {@link Logger} that is more user friendly and has a few advanced operations.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AdvancedLogger {

  public static final AdvancedLogger NULL = new AdvancedLogger();

  private final Logger delegate;

  private AdvancedLogger() {
    delegate = Logger.getLogger("com.morgan.null");
    delegate.setLevel(Level.OFF);
  }

  public AdvancedLogger(Class<?> loggingClass) {
    delegate = Logger.getLogger(loggingClass.getName());
  }

  @VisibleForTesting void doLog(Level logLevel, @Nullable Throwable e, String fmt, Object... args) {
    if (e == null) {
      delegate.log(logLevel, String.format(fmt, args));
    } else {
      delegate.log(logLevel, String.format(fmt, args), e);
    }
  }

  /** As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#FINEST}. */
  public void trace(String fmt, Object... args) {
    doLog(Level.FINEST, null, fmt, args);
  }

  /**
   * As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#FINEST} and an
   * exception.
   */
  public void trace(Throwable e, String fmt, Object... args) {
    doLog(Level.FINEST, e, fmt, args);
  }

  /** As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#FINER}. */
  public void fineDebug(String fmt, Object... args) {
    doLog(Level.FINER, null, fmt, args);
  }

  /**
   * As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#FINER} and an
   * exception.
   */
  public void fineDebug(Throwable e, String fmt, Object... args) {
    doLog(Level.FINER, e, fmt, args);
  }

  /** As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#FINE}. */
  public void debug(String fmt, Object... args) {
    doLog(Level.FINE, null, fmt, args);
  }

  /**
   * As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#FINE} and an
   * exception.
   */
  public void debug(Throwable e, String fmt, Object... args) {
    doLog(Level.FINE, e, fmt, args);
  }

  /** As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#INFO}. */
  public void info(String fmt, Object... args) {
    doLog(Level.INFO, null, fmt, args);
  }

  /**
   * As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#INFO} and an
   * exception.
   */
  public void info(Throwable e, String fmt, Object... args) {
    doLog(Level.INFO, e, fmt, args);
  }

  /** As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#WARNING}. */
  public void warning(String fmt, Object... args) {
    doLog(Level.WARNING, null, fmt, args);
  }

  /**
   * As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#WARNING} and an
   * exception.
   */
  public void warning(Throwable e, String fmt, Object... args) {
    doLog(Level.WARNING, e, fmt, args);
  }

  /** As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#SEVERE}. */
  public void error(String fmt, Object... args) {
    doLog(Level.SEVERE, null, fmt, args);
  }

  /**
   * As {@link Logger#log(Level, String, Object[])} with a level of {@link Level#SEVERE} and an
   * exception.
   */
  public void error(Throwable e, String fmt, Object... args) {
    doLog(Level.SEVERE, e, fmt, args);
  }

  /**
   * Creates a {@link PrintWriter} that, when closed, publishes a single log message at
   * the given log level and with the given exception.
   */
  public PrintWriter logWriter(Throwable cause, Level logLevel) {
    return new PrintWriter(new LoggingOutputWriter(cause, logLevel));
  }

  /**
   * As {@link #logWriter(Throwable, Level)}, but with no exception.
   */
  public PrintWriter logWriter(Level logLevel) {
    return new PrintWriter(new LoggingOutputWriter(null, logLevel));
  }

  /**
   * Implementation of a {@link StringWriter} that stores a log level and exception and logs the
   * output when closed.
   */
  private class LoggingOutputWriter extends StringWriter {

    @Nullable private final Throwable cause;
    private final Level logLevel;

    private boolean closed = false;

    private LoggingOutputWriter(@Nullable Throwable cause, Level level) {
      this.cause = cause;
      this.logLevel = Preconditions.checkNotNull(level);
    }

    @Override public void close() throws IOException {
      Preconditions.checkState(!closed);
      closed = true;
      super.close();

      doLog(logLevel, cause, super.toString());
    }
  }
}
