/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

import java.util.List;

public class AnalysisInput {
  private String raw;
  private List<String> expected;

  public String getRaw() {
    return raw;
  }

  public void setRaw(String raw) {
    this.raw = raw;
  }

  public List<String> getExpected() {
    return expected;
  }

  public void setExpected(List<String> expected) {
    this.expected = expected;
  }
}
