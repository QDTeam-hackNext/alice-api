/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import application.JsonTracer;
import data.Policy;
import data.QuickQuoteInput;
import data.QuickQuoteResult;
import service.Network.Result;

public class AlicePolicy {
  private static final Logger LOG = LoggerFactory.getLogger(AlicePolicy.class);

  private final Network network;

  public AlicePolicy() {
    this.network = new Network();
  }

  public QuickQuoteResult quickQuote(QuickQuoteInput input) {
    Result<QuickQuoteResult> result = network.post(
        "https://www.allianz.de/oneweb/ajax/aspro/multiofferlebenservice/quickquote", input, QuickQuoteResult.class);
    if (result.ok) {
      return result.value;
    }

    LOG.warn("Result failure: {}", new JsonTracer(result));
    throw new RuntimeException("Getting quick quote failed");
  }

  public List<Policy> getPoliciesForUser(String userId, boolean includeQuotes) {
    // stub couple of policies and quotes that user has
    return ImmutableList.<Policy>builder()
        .add(new Policy.Builder()
            .withPolicyId(String.format("%s-123456", Policy.Type.House.name()))
            .withType(Policy.Type.House)
            .withPremium(new Policy.Value("EUR", 568.00))
            .withQuote(false)
            .withValidUntil(getValidUntil(10))
            .withDescription("Comfortable hause insurance. Covers everything from natual disasters up to your"
                + " friends with babies vist (which could be even more devastating ;)).")
            .withTermsUrl("https://www.example.org/house/terms_and_conditions.html")
            .build())
        .add(new Policy.Builder()
            .withPolicyId(String.format("%s-55455", Policy.Type.Health.name()))
            .withType(Policy.Type.Health)
            .withPremium(new Policy.Value("EUR", 1200.00))
            .withQuote(false)
            .withValidUntil(getValidUntil(50))
            .withDescription("It is said that health is priceless but in case something happens you are covered.")
            .withTermsUrl("https://www.example.org/health/terms_and_conditions.html")
            .build())
        .add(new Policy.Builder()
            .withPolicyId(String.format("%s-007632", Policy.Type.Car.name()))
            .withType(Policy.Type.Car)
            .withPremium(new Policy.Value("EUR", 700.00))
            .withQuote(false)
            .withValidUntil(getValidUntil(20))
            .withDescription("Wether your are flasher, blinker or steady rider. You can count on your car insurance.")
            .withTermsUrl("https://www.example.org/car/terms_and_conditions.html")
            .build())
        .add(new Policy.Builder()
            .withPolicyId(String.format("%s-341109", Policy.Type.Bike.name()))
            .withType(Policy.Type.Bike)
            .withPremium(new Policy.Value("EUR", 70.00))
            .withQuote(includeQuotes)
            .withValidUntil(getValidUntil(120))
            .withDescription("Your precious bike is covered.")
            .withTermsUrl("https://www.example.org/bike/terms_and_conditions.html")
            .build())
        .build();
  }

  private String getValidUntil(int validDays) {
    DateTime utcEventTime = DateTime.now().plusDays(validDays).toDateTime(DateTimeZone.UTC);
    return ISODateTimeFormat.dateTime().print(utcEventTime);
  }
}
