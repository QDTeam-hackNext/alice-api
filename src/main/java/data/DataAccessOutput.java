/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

import java.util.List;

import com.ibm.watson.developer_cloud.conversation.v1.model.Entity;

public class DataAccessOutput {
  public final String id;
  public final boolean accessGranted;
  public final String message;
  public final List<Entity> entities;

  public DataAccessOutput(String id, boolean accessGranted, String message, List<Entity> entities) {
    this.id = id;
    this.accessGranted = accessGranted;
    this.message = message;
    this.entities = entities;
  }
}
