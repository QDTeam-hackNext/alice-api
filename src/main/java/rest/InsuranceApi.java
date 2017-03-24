/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import data.AnalysisInput;
import data.AnalysisOutput;
import data.ConversationInput;
import data.QuoteOutput;
import service.AliceConv;
import service.AliceNlu;

@ApplicationPath("api")
@Path("/")
public class InsuranceApi extends Application {
  private final Gson gson = new Gson();
  private final AliceNlu aliceNlu = new AliceNlu();
  private final AliceConv aliceConv = new AliceConv();

  @GET
  @Path("endpoints/")
  @Produces({"application/json"})
  public String services() {
    String[] services = new String[] {"api/endpoints", "conversation/data_access", "api/analysis", "api/quote"};
    return gson.toJson(services);
  }

  @POST
  @Path("conversation/data_access/")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String dataAccessConversation(ConversationInput input) {
    Entry<String, MessageResponse> response = aliceConv.dataAccess(input.getInput(), input.getId());
    return gson.toJson(response);
  }

  @POST
  @Path("analysis/")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String analysis(AnalysisInput input) {
    List<String> keywords = aliceNlu.keywords(input.getRaw());
    Map<String, String> fields = new HashMap<>(4);
    fields.put("keywords", Joiner.on(',').join(keywords));
    fields.put("birth-date", "23.05.1977");
    fields.put("occupation", "designer");
    fields.put("marital-status", "single");
    return gson.toJson(new AnalysisOutput(fields));
  }

  @POST
  @Path("quote/")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String quote() {
    return gson.toJson(new QuoteOutput("250"));
  }
}
