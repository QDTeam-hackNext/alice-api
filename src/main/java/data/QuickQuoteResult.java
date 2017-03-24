/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

public class QuickQuoteResult {
  public enum Status {
    OK, @SerializedName("NOK")
    FAIL;

    public static GsonBuilder registerSerializer(GsonBuilder builder) {
      builder.registerTypeAdapter(Status.class, new JsonSerializer<Status>() {
        @Override
        public JsonElement serialize(Status src, Type typeOfSrc, JsonSerializationContext context) {
          return new JsonPrimitive(src.name());
        }
      });
      return builder;
    }
  }

  public static class Value {
    private Double brutto;
    private Double netto;
    @SerializedName("verrechnung")
    private Double provision;

    public static GsonBuilder registerSerializer(GsonBuilder builder) {
      builder.registerTypeAdapter(Value.class, new JsonSerializer<Value>() {
        @Override
        public JsonElement serialize(Value src, Type typeOfSrc, JsonSerializationContext context) {
          JsonObject json = new JsonObject();
          json.addProperty("brutto", src.brutto);
          json.addProperty("netto", src.netto);
          json.addProperty("provision", src.provision);
          return json;
        }
      });
      return builder;
    }

    public Double getBrutto() {
      return brutto;
    }

    public void setBrutto(Double brutto) {
      this.brutto = brutto;
    }

    public Double getNetto() {
      return netto;
    }

    public void setNetto(Double netto) {
      this.netto = netto;
    }

    public Double getProvision() {
      return provision;
    }

    public void setProvision(Double provision) {
      this.provision = provision;
    }
  }

  public static class Error {
    private String id;
    private String text;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }

  private Status status;
  @SerializedName("beitrag")
  private Value premium;
  private Error error;
  @SerializedName("pruefoption")
  private boolean check;

  public static GsonBuilder registerSerializer(GsonBuilder builder) {
    builder.registerTypeAdapter(QuickQuoteResult.class, new JsonSerializer<QuickQuoteResult>() {
      @Override
      public JsonElement serialize(QuickQuoteResult src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add("status", context.serialize(src.status));
        if (src.premium != null) {
          json.add("premium", context.serialize(src.premium));
        }
        if (src.error != null) {
          json.add("error", context.serialize(src.error));
        }
        json.addProperty("check", src.check);
        return json;
      }
    });
    Status.registerSerializer(builder);
    Value.registerSerializer(builder);
    return builder;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Value getPremium() {
    return premium;
  }

  public void setPremium(Value premium) {
    this.premium = premium;
  }

  public Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
  }

  public boolean isCheck() {
    return check;
  }

  public void setCheck(boolean check) {
    this.check = check;
  }
}
