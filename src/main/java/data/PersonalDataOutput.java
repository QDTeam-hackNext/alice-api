/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;

public class PersonalDataOutput {
  public static class Field {
    public final String name;
    public final String value;

    public Field(String name, String value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("name", name).add("value", value).toString();
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, value);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Field) {
        Field other = (Field) obj;
        return Objects.equals(name, other.name)
            && Objects.equals(value, other.value);
      }

      return false;
    }
  }

  public final String id;
  public final String message;
  public final List<Field> fields;
  public final boolean collected;

  public static PersonalDataOutput collected(List<Field> fields) {
    return new PersonalDataOutput(null, null, fields, true);
  }

  public PersonalDataOutput(String id, String message, List<Field> fields, boolean collected) {
    this.id = id;
    this.message = message;
    this.fields = fields;
    this.collected = collected;
  }
}
