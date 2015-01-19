package com.morgan.client.alert;

/**
 * Builder interface for building new alerts.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AlertBuilder {
  /**
   * Sets the alert's priority.  Higher priority items (those with higher values) are displayed
   * higher in the alert display. Status messages by default have a priority of 1000 while error
   * messages have a default priority of 0.
   */
  AlertBuilder setPriority(int priority);

  /**
   * Sets whether or not this alert fades over time (by default, error alerts do not fade over time
   * and status alerts do).
   */
  AlertBuilder isFading(boolean isFading);

  /**
   * Adds a style name to this alert.
   */
  AlertBuilder addStyleName(String styleName);

  /**
   * Creates the alert represented by this builder.  This does not mean the alert is
   * displayed.  In order to display the alert, you have to call {@link Alert#requestDisplay()}.
   */
  Alert create();
}
