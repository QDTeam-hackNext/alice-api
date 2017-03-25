/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTracer {
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private final Object toTrace;

  public JsonTracer(Object toTrace) {
    this.toTrace = toTrace;
  }

  @Override
  public String toString() {
    return gson.toJson(toTrace);
  }
}
