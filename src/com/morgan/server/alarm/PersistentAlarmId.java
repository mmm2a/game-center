package com.morgan.server.alarm;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Persistent implementation of the {@link AlarmId} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class PersistentAlarmId implements AlarmId {

  private final long key;

  public PersistentAlarmId(long key) {
    this.key = key;
  }

  @Override public boolean isPersistent() {
    return true;
  }

  public long getKey() {
    return key;
  }

  @Override public int hashCode() {
    return Objects.hash(key);
  }

  @Override public boolean equals(Object o) {
    if (!(o instanceof PersistentAlarmId)) {
      return false;
    }

    return key == ((PersistentAlarmId) o).key;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(PersistentAlarmId.class)
        .add("key", key)
        .toString();
  }
}
