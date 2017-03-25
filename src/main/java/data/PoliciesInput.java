/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

public class PoliciesInput {
  private String userId;
  private boolean includeQuotes;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public boolean isIncludeQuotes() {
    return includeQuotes;
  }

  public void setIncludeQuotes(boolean includeQuotes) {
    this.includeQuotes = includeQuotes;
  }
}
