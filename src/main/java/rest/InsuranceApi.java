/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;

import data.ConversationInput;
import data.DataAccessOutput;
import data.PersonalDataConversationInput;
import data.PersonalDataOutput;
import data.PersonalDataOutput.Field;
import data.PoliciesInput;
import data.QuickQuoteInput;
import data.QuickQuoteResult;
import service.AliceConv;
import service.AliceNlu;
import service.AlicePolicy;
import utils.JsonTracer;

@ApplicationPath("api")
@Path("/")
public class InsuranceApi extends Application {
  private static final Logger LOG = LoggerFactory.getLogger(InsuranceApi.class);

  private final Gson gson = QuickQuoteResult.registerSerializer(new GsonBuilder()).setPrettyPrinting().create();
  private final AliceNlu aliceNlu = new AliceNlu();
  private final AliceConv aliceConv = new AliceConv();
  private final AlicePolicy policies = new AlicePolicy();

  @GET
  @Path("endpoints/")
  @Produces({"application/json"})
  public String services() {
    String[] services = new String[] { "api/endpoints", "conversation/data_access", "conversation/data_access",
        "policies", "policies/quick_quote" };
    return gson.toJson(services);
  }

  @POST
  @Path("conversation/data_access/")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String dataAccessConversation(ConversationInput input) {
    LOG.debug("Input value: {}", new JsonTracer(input));
    DataAccessOutput response = aliceConv.dataAccess(input.getInput(), input.getId());
    return gson.toJson(response);
  }

  @POST
  @Path("conversation/personal_data/")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String personalDataConversation(PersonalDataConversationInput input) {
    LOG.debug("Input value: {}", new JsonTracer(input));
    List<PersonalDataOutput.Field> fields = Collections.emptyList();
    List<String> required = new ArrayList<>(input.getRequired());
    ImmutableMap.Builder<String, Object> builder =
        ImmutableMap.<String, Object>builder().put("username", input.getUsername());

    //perform input text analysis
    PersonalDataOutput result;
    if (Strings.isNullOrEmpty(input.getId())) {
      // mock analysis for now ;)
      if ("Trust me I'm an engineer :)".equals(input.getInput())) {
        fields = new ImmutableList.Builder<PersonalDataOutput.Field>()
            .add(new PersonalDataOutput.Field("occupation", "designer")) // JobTitle NLU entity
            .add(new PersonalDataOutput.Field("healthy", "very healthy")) // no such entity has to be asked for
            .add(new PersonalDataOutput.Field("sport", "running")) // Sport NLU entity
            .build();
      } else if (!Strings.isNullOrEmpty(input.getInput())) {
        fields = FluentIterable.from(aliceNlu.entities(input.getInput()))
            .transform(new Function<EntitiesResult, PersonalDataOutput.Field>() {
              @Override
              public PersonalDataOutput.Field apply(EntitiesResult input) {
                return new PersonalDataOutput.Field(input.getType(), input.getText());
              }
            }).toList();
      }

      markFound(fields, required);
      if (required.isEmpty()) {
        return gson.toJson(PersonalDataOutput.collected(fields));
      }

      result = aliceConv.personalData(null, null, builder.put("required", required.get(0)).build());
    } else {
      result = aliceConv.personalData(input.getInput(), input.getId(),
          builder.put("required", required.get(0)).build());
    }

    markFound(result.fields, required);
    if (required.isEmpty()) {
      return gson.toJson(PersonalDataOutput.collected(fields));
    }
    return gson.toJson(result);
  }

  @POST
  @Path("policies/")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String policies(PoliciesInput input) {
    LOG.debug("Input value: {}", new JsonTracer(input));
    return gson.toJson(policies.getPoliciesForUser(input.getUserId(), input.isIncludeQuotes()));
  }

  @POST
  @Path("policies/quick_quote")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String quickQuote(QuickQuoteInput input) {
    LOG.debug("Input value: {}", new JsonTracer(input));
    // hack default occupation for now
    if (Strings.isNullOrEmpty(input.getData().getOccupation())) {
      input.getData().setOccupation("Designer(in)");
    }
    return gson.toJson(policies.quickQuote(input));
  }

  private void markFound(List<PersonalDataOutput.Field> fields, List<String> required) {
    if (fields.isEmpty()) {
      return;
    }

    for (Field field : fields) {
      required.remove(field.name);
    }
  }
}
