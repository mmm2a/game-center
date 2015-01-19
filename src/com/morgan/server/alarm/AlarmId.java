package com.morgan.server.alarm;


/**
 * Interface for an alarm ID.  This interface is used so that for persistent alarms (for which
 * clients MIGHT need to store something in a database), they can be stored as, and re-created from
 * a VERY simple data type; a long.  For in memory alarms, the handle is more opaque owing to the
 * fact that clients can always store them in memory somehow.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AlarmId {
  /**
   * Indicates whether or not this alarm id represents a persistent one (and thus can exist
   * beyond server restarts).  If this method returns {@code true}, then its guaranteed that the
   * implementation is of this interface is {@link PersistentAlarmId}.
   */
  boolean isPersistent();
}
