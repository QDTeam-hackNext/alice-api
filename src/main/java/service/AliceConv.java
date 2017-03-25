/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Entity;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import data.DataAccessOutput;
import data.PersonalDataOutput;
import data.PersonalDataOutput.Field;

public class AliceConv extends AliceWatsonService<ConversationService> {
  private static final String WORKSPACE_DATA_ACCESS = "89247bc0-0d49-40b0-a355-522145254f18";
  private static final String WORKSPACE_PERSONAL_DATA = "c33978ec-e370-484d-be94-2c4e1f18af0c";

  private final Cache<String, Map<String, Object>> contexts;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public AliceConv() {
    super(new ConversationService(ConversationService.VERSION_DATE_2017_02_03),
        "5210e9bf-24fa-46fe-8ae9-9bf925c8574d",
        "buFTjfAGIaxQ",
        "https://gateway.watsonplatform.net/conversation/api");
    
    contexts = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build();
  }

  public PersonalDataOutput personalData(String input, String id, Map<String, Object> context) {
    Entry<String, MessageResponse> msg = message(input, id, WORKSPACE_PERSONAL_DATA, context);
    System.out.println("personalData message return:" + gson.toJson(msg));
    List<Field> fields = FluentIterable.from(msg.getValue().getEntities())
        .transform(new Function<Entity, PersonalDataOutput.Field>() {
          @Override
          public PersonalDataOutput.Field apply(Entity input) {
            return new PersonalDataOutput.Field(input.getEntity(), input.getValue());
          }
        }).toList();
    return new PersonalDataOutput(msg.getKey(), msg.getValue().getTextConcatenated(" "), fields, false);
  }

  public DataAccessOutput dataAccess(String input, String id) {
    Entry<String, MessageResponse> msg = message(input, id, WORKSPACE_DATA_ACCESS, null);
    System.out.println("dataAccess message return:" + gson.toJson(msg));
    MessageResponse data = msg.getValue();
    List<Intent> intents = data.getIntents();
    boolean accessGranted = intents.size() >= 1 && intents.get(0).getIntent().equalsIgnoreCase("ok")
        && intents.get(0).getConfidence() > 0.6;
    return new DataAccessOutput(msg.getKey(), accessGranted, msg.getValue().getTextConcatenated(" "),
        msg.getValue().getEntities());
  }

  private Entry<String, MessageResponse> message(String input, String id, String workspaceId,
      Map<String, Object> additionalContext) {
    Map<String, Object> context = mergeContext(id, additionalContext);
    MessageRequest.Builder builder = new MessageRequest.Builder();
    if (!Strings.isNullOrEmpty(input)) {
      builder.inputText(input);
    }
    MessageRequest req = builder.alternateIntents(true).context(context).build();
    MessageResponse resp = service.message(workspaceId, req).execute();
    context = resp.getContext();
    if (context == null) {
      return Maps.immutableEntry(null, resp);
    }

    id = (String) context.get("conversation_id");
    contexts.put(id, context);
    return Maps.immutableEntry(id, resp);
  }

  private Map<String, Object> mergeContext(String id, Map<String, Object> additionalContext) {
    if (Strings.isNullOrEmpty(id)) {
      return additionalContext;
    }

    Map<String, Object> existing = contexts.getIfPresent(id);
    if (existing == null) {
      return additionalContext;
    }

    if (additionalContext != null) {
      existing.putAll(additionalContext);
    }
    return existing;
  }
}
