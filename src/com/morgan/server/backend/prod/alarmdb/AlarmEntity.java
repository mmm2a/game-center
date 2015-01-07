package com.morgan.server.backend.prod.alarmdb;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Entity type for representing a stored/persistent alarm.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@NamedQueries({
  @NamedQuery(
      name = "findAllAlarms",
      query = "SELECT a FROM alarminfo AS a")
})
@Entity(name = "alarminfo")
class AlarmEntity {

  @Id @GeneratedValue
  @VisibleForTesting long id;

  @Column(length = 512, nullable = false)
  private String alarmCallbackClass;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date nextOccurrence;

  @Column(nullable = true)
  @Nullable private Long repeatInterval;

  @Lob
  @Column(length = 4096, nullable = true)
  @Nullable private Serializable alarmData;

  AlarmEntity() {
  }

  AlarmEntity(
      Date nextOccurrence,
      String alarmCallbackClass,
      @Nullable Long repeatInterval,
      @Nullable Serializable alarmData) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(alarmCallbackClass));

    this.alarmCallbackClass = alarmCallbackClass;
    this.nextOccurrence = Preconditions.checkNotNull(nextOccurrence);
    this.repeatInterval = repeatInterval;
    this.alarmData = alarmData;
  }

  long getId() {
    return id;
  }

  String getAlarmCallbackClass() {
    return alarmCallbackClass;
  }

  Date getNextOccurrence() {
    return nextOccurrence;
  }

  void setNextOccurrence(Date nextOccurrence) {
    this.nextOccurrence = Preconditions.checkNotNull(nextOccurrence);
  }

  @Nullable Long getRepeatInterval() {
    return repeatInterval;
  }

  @Nullable Serializable getAlarmData() {
    return alarmData;
  }
  
  @Override public int hashCode() {
    return Objects.hash(id, alarmCallbackClass, nextOccurrence, repeatInterval, alarmData);
  }
  
  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    
    if (!(o instanceof AlarmEntity)) {
      return false;
    }
    
    AlarmEntity other = (AlarmEntity) o;
    
    return id == other.id
        && alarmCallbackClass.equals(other.alarmCallbackClass)
        && nextOccurrence.equals(other.nextOccurrence)
        && Objects.equals(repeatInterval, other.repeatInterval)
        && Objects.equals(alarmData, other.alarmData);
  }
  
  @Override public String toString() {
    return MoreObjects.toStringHelper(AlarmEntity.class)
        .add("id", id)
        .add("alarmCallbackClass", alarmCallbackClass)
        .add("nextOccurrence", nextOccurrence)
        .add("repeatInterval", repeatInterval)
        .add("alarmData", alarmData)
        .toString();
  }
}
