/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

import java.util.List;

public class PersonalDataConversationInput extends ConversationInput {
  private String username;
  private List<String> required;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRequired() {
    return required;
  }

  public void setRequired(List<String> required) {
    this.required = required;
  }
}
