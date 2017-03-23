/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import com.google.gson.Gson;

import data.AnalysisInput;
import data.AnalysisOutput;
import data.QuoteOutput;

@ApplicationPath("api")
@Path("/")
public class InsuranceApi extends Application {
  private final Gson gson = new Gson();

  @GET
  @Path("endpoints/")
  @Produces({"application/json"})
  public String services() {
    String[] services = new String[] {"api/endpoints", "api/analysis", "api/quote"};
    return gson.toJson(services);
  }

  @POST
  @Path("analysis/")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String analysis(AnalysisInput input) {
    Map<String, String> fields = new HashMap<>(3);
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
