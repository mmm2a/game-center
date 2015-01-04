package com.morgan.server.backend;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.Nullable;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.morgan.server.backend.AlarmBackend.PersistedAlarmDescription;

/**
 * Default implementation of the {@link PersistedAlarmDescription} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class DefaultPersistedAlarmDescription implements PersistedAlarmDescription {

  private final long id;
  private final ReadableInstant nextDeadline;
  private final ReadableDuration repeatInterval;
  private final String callbackClass;
  private final Serializable alarmData;

  public DefaultPersistedAlarmDescription(
      long id,
      ReadableInstant nextDeadline,
      @Nullable ReadableDuration repeatInterval,
      String callbackClass,
      @Nullable Serializable alarmData) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(callbackClass));

    this.id = id;
    this.nextDeadline = Preconditions.checkNotNull(nextDeadline);
    this.repeatInterval = repeatInterval;
    this.callbackClass = callbackClass;
    this.alarmData = alarmData;
  }

  @Override public long getId() {
    return id;
  }

  @Override public ReadableInstant getNextDeadline() {
    return nextDeadline;
  }

  @Nullable @Override public ReadableDuration getRepeatInterval() {
    return repeatInterval;
  }

  @Override public String getAlarmCallbackClass() {
    return callbackClass;
  }

  @Nullable @Override public Serializable getAlarmData() {
    return alarmData;
  }

  @Override public int hashCode() {
    return Objects.hash(id, nextDeadline, repeatInterval, callbackClass, alarmData);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof DefaultPersistedAlarmDescription)) {
      return false;
    }

    DefaultPersistedAlarmDescription other = (DefaultPersistedAlarmDescription) o;
    return id == other.id
        && nextDeadline.equals(other.nextDeadline)
        && Objects.equals(repeatInterval, other.repeatInterval)
        && callbackClass.equals(other.callbackClass)
        && Objects.equals(alarmData, other.alarmData);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(DefaultPersistedAlarmDescription.class)
        .add("id", id)
        .add("nextDeadline", nextDeadline)
        .add("repeatInterval", repeatInterval)
        .add("callbackClass", callbackClass)
        .add("alarmData", alarmData)
        .toString();
  }
}
