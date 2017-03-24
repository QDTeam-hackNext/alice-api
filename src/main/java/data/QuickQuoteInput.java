/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package data;

import com.google.gson.annotations.SerializedName;

public class QuickQuoteInput {
  public static class Agreement {
    public enum Type {
      @SerializedName("RLV_BASIS")
      RLV,
      @SerializedName("SBV_BASIS")
      SBV
    }

    @SerializedName("produkt")
    private Type product;
    @SerializedName("betrag")
    private Double sum;
    @SerializedName("beginn")
    private String startDate;
    @SerializedName("vertragslaufzeit")
    private Integer period;
    @SerializedName("raucherstatus")
    private Boolean smokes;

    public Type getProduct() {
      return product;
    }

    public void setProduct(Type product) {
      this.product = product;
    }

    public Double getSum() {
      return sum;
    }

    public void setSum(Double sum) {
      this.sum = sum;
    }

    public String getStartDate() {
      return startDate;
    }

    public void setStartDate(String startDate) {
      this.startDate = startDate;
    }

    public Integer getPeriod() {
      return period;
    }

    public void setPeriod(Integer period) {
      this.period = period;
    }

    public Boolean getSmokes() {
      return smokes;
    }

    public void setSmokes(Boolean smokes) {
      this.smokes = smokes;
    }
  }

  public static class PersonalData {
    @SerializedName("geburtsdatum")
    private String birthDate;
    @SerializedName("berufeingabe")
    private String occupation;

    public String getBirthDate() {
      return birthDate;
    }

    public void setBirthDate(String birthDate) {
      this.birthDate = birthDate;
    }

    public String getOccupation() {
      return occupation;
    }

    public void setOccupation(String occupation) {
      this.occupation = occupation;
    }
  }

  @SerializedName("vertrag")
  private Agreement agreement;
  @SerializedName("vp")
  private PersonalData data;

  public Agreement getAgreement() {
    return agreement;
  }

  public void setAgreement(Agreement agreement) {
    this.agreement = agreement;
  }

  public PersonalData getData() {
    return data;
  }

  public void setData(PersonalData data) {
    this.data = data;
  }
}
