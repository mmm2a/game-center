package com.morgan.testing;

import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.truth.FailureStrategy;
import com.google.common.truth.Subject;
import com.google.common.truth.SubjectFactory;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.morgan.client.alert.Alert;
import com.morgan.client.alert.AlertBuilder;
import com.morgan.client.alert.AlertController;
import com.morgan.client.alert.AlertHandle;

/**
 * Fake version of the {@link AlertController} to help with testing that involves
 * alerts.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FakeAlertController implements AlertController {
  
  private final Multimap<FakeAlert, FakeAlertHandle> shownAlerts = HashMultimap.create();
  
  @Override public FakeAlertBuilder newStatusAlertBuilder(String text) {
    return newStatusAlertBuilder(SafeHtmlUtils.fromString(text));
  }

  @Override public FakeAlertBuilder newErrorAlertBuilder(String text) {
    return newErrorAlertBuilder(SafeHtmlUtils.fromString(text));
  }

  @Override public FakeAlertBuilder newStatusAlertBuilder(SafeHtml contents) {
    return newStatusAlertBuilder(new FakeIsWidget(contents));
  }

  @Override public FakeAlertBuilder newErrorAlertBuilder(SafeHtml contents) {
    return newErrorAlertBuilder(new FakeIsWidget(contents));
  }

  @Override public FakeAlertBuilder newStatusAlertBuilder(IsWidget contents) {
    return new FakeAlertBuilder(contents, false);
  }

  @Override public FakeAlertBuilder newErrorAlertBuilder(IsWidget contents) {
    return new FakeAlertBuilder(contents, true);
  }
  
  /**
   * Fake implementation of the {@link IsWidget} interface to aid in alert testing.
   */
  private class FakeIsWidget implements IsWidget {

    private final SafeHtml message;
    
    FakeIsWidget(SafeHtml message) {
      this.message = Preconditions.checkNotNull(message);
    }
    
    @Override public Widget asWidget() {
      throw new UnsupportedOperationException();
    }
    
    @Override public int hashCode() {
      return message.hashCode();
    }
    
    @Override public boolean equals(Object o) {
      return (o instanceof FakeIsWidget)
          && ((FakeIsWidget) o).message.equals(message);
    }
    
    @Override public String toString() {
      return "Alert<" + message.asString() + ">";
    }
  }
  
  /**
   * Fake version of the {@link AlertBuilder} interface for testing purposes.
   */
  public class FakeAlertBuilder implements AlertBuilder {

    private final IsWidget message;
    private final boolean isErrorAlert;
    
    private final ImmutableSet.Builder<String> additionalStyleNames =
        ImmutableSet.builder();
    
    @Nullable private Integer priority;
    @Nullable private Boolean isFading;
    
    FakeAlertBuilder(IsWidget message, boolean isErrorAlert) {
      this.message = Preconditions.checkNotNull(message);
      this.isErrorAlert = isErrorAlert;
    }
    
    @Override public FakeAlertBuilder setPriority(int priority) {
      this.priority = priority;
      return this;
    }

    @Override public FakeAlertBuilder isFading(boolean isFading) {
      this.isFading = isFading;
      return this;
    }

    @Override public FakeAlertBuilder addStyleName(String styleName) {
      additionalStyleNames.add(styleName);
      return this;
    }

    @Override public FakeAlert create() {
      return new FakeAlert(
          message,
          isErrorAlert,
          additionalStyleNames.build(),
          priority,
          isFading);
    }
  }
  
  /**
   * Fake version of the {@link Alert} interface for testing purposes.
   */
  public class FakeAlert implements Alert {

    private final IsWidget message;
    private final boolean isErrorAlert;
    private final ImmutableSet<String> additionalStyleNames;
    @Nullable private final Integer priority;
    @Nullable private final Boolean isFading;
    
    FakeAlert(
        IsWidget message,
        boolean isErrorAlert,
        ImmutableSet<String> additionalStyleNames,
        @Nullable Integer priority,
        @Nullable Boolean isFading) {
      this.message = Preconditions.checkNotNull(message);
      this.isErrorAlert = isErrorAlert;
      this.additionalStyleNames = additionalStyleNames;
      this.priority = priority;
      this.isFading = isFading;
    }
    
    /**
     * Indicates whether or not this fake alert is currently being displayed.
     */
    boolean isDisplayed() {
      return !shownAlerts.get(this).isEmpty();
    }
    
    @Override public int hashCode() {
      return Objects.hash(
          message,
          isErrorAlert,
          additionalStyleNames,
          priority,
          isFading);
    }
    
    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      
      if (!(o instanceof FakeAlert)) {
        return false;
      }
      
      FakeAlert other = (FakeAlert) o;
      return message.equals(other.message)
          && isErrorAlert == other.isErrorAlert
          && additionalStyleNames.equals(other.additionalStyleNames)
          && Objects.equals(priority, other.priority)
          && Objects.equals(isFading, other.isFading);
    }
    
    @Override public String toString() {
      return MoreObjects.toStringHelper(FakeAlert.class)
          .add("message", message)
          .add("isErrorAlert", isErrorAlert)
          .add("additionalStyleNames", additionalStyleNames)
          .add("priority", priority)
          .add("isFading", isFading)
          .toString();
    }
    
    @Override public AlertHandle requestDisplay() {
      return new FakeAlertHandle(this);
    }
  }
  
  /**
   * Fake implementation of the {@link AlertHandle} interface for testing.
   */
  private class FakeAlertHandle implements AlertHandle {

    private final FakeAlert responsibleAlert;
    
    FakeAlertHandle(FakeAlert responsibleAlert) {
      this.responsibleAlert = responsibleAlert;
      shownAlerts.put(responsibleAlert, this);
    }
    
    @Override public void cancel() {
      shownAlerts.remove(responsibleAlert, this);
    }

    @Override public <T> AsyncCallback<T> delegateTo(final AsyncCallback<T> delegate) {
      return new AsyncCallback<T>() {

        @Override public void onFailure(Throwable caught) {
          cancel();
          delegate.onFailure(caught);
        }

        @Override public void onSuccess(T result) {
          cancel();
          delegate.onSuccess(result);
        }
      };
    }
  }
  
  /**
   * {@link Subject} for testing alerts.
   */
  public static class AlertSubject extends Subject<AlertSubject, FakeAlert> {
    public AlertSubject(FailureStrategy failureStrategy, FakeAlert subject) {
      super(failureStrategy, subject);
    }

    public void isDisplayed() {
      if (!getSubject().isDisplayed()) {
        fail("is displayed");
      }
    }
    
    public void isNotDisplayed() {
      if (getSubject().isDisplayed()) {
        fail("is not displayed");
      }
    }
  }
  
  /**
   * {@link SubjectFactory} for testing alerts.  See {@link FakeAlertController#ALERT} for
   * usage.
   */
  public static class AlertSubjectFactory extends SubjectFactory<AlertSubject, FakeAlert> {
    @Override public AlertSubject getSubject(FailureStrategy fs, FakeAlert that) {
      return new AlertSubject(fs, that);
    }
  }
  
  /**
   * Public {@link SubjectFactory} for testing alerts.  To use this subject, use a call
   * like <code>assertAbout(ALERT).that(someAlert).isDisplayed</code>
   */
  public static final AlertSubjectFactory ALERT = new AlertSubjectFactory();
}
