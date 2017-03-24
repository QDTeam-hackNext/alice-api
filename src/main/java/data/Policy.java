/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

public class Policy {
  public static class Builder {
    private String policyId;
    private Type type;
    private boolean quote;
    private Value premium;
    private String description;
    private String termsUrl;
    private String validUntil;

    public Builder withPolicyId(String policyId) {
      this.policyId = policyId;
      return this;
    }

    public Builder withType(Type type) {
      this.type = type;
      return this;
    }

    public Builder withQuote(boolean quote) {
      this.quote = quote;
      return this;
    }

    public Builder withPremium(Value premium) {
      this.premium = premium;
      return this;
    }

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder withTermsUrl(String termsUrl) {
      this.termsUrl = termsUrl;
      return this;
    }

    public Builder withValidUntil(String validUntil) {
      this.validUntil = validUntil;
      return this;
    }

    public Policy build() {
      return new Policy(policyId, type, quote, premium, validUntil, description, termsUrl);
    }
  }

  public enum Type {
    House, Car, Bike, Health;
  }

  public static class Value {
    public final String code;
    public final Double value;

    public Value(String code, Double value) {
      this.code = code;
      this.value = value;
    }
  }

  public final String policyId;
  public final Type type;
  public final boolean quote;
  public final Value premium;
  public final String validUntil;
  public final String description;
  public final String termsUrl;

  private Policy(String policyId, Type type, boolean quote, Value premium, String validUntil, String description,
      String termsUrl) {
    this.policyId = policyId;
    this.type = type;
    this.quote = quote;
    this.premium = premium;
    this.validUntil = validUntil;
    this.description = description;
    this.termsUrl = termsUrl;
  }
}
