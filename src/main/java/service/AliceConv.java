/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

public class AliceConv extends AliceWatsonService<ConversationService> {
  private static final String WORKSPACE_DATA_ACCESS = "89247bc0-0d49-40b0-a355-522145254f18";

  private final Cache<String, Map<String, Object>> contexts;

  public AliceConv() {
    super(new ConversationService(ConversationService.VERSION_DATE_2017_02_03),
        "5210e9bf-24fa-46fe-8ae9-9bf925c8574d",
        "buFTjfAGIaxQ",
        "https://gateway.watsonplatform.net/conversation/api");
    
    contexts = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
  }

  public Entry<String, MessageResponse> dataAccess(String input, String id) {
    return message(input, id, WORKSPACE_DATA_ACCESS);
  }

  private Entry<String, MessageResponse> message(String input, String id, String workspaceId) {
    Map<String, Object> context = Strings.isNullOrEmpty(id) ? null : contexts.getIfPresent(id);
    MessageRequest req = new MessageRequest.Builder().inputText(input).alternateIntents(true).context(context).build();
    MessageResponse resp = service.message(workspaceId, req).execute();
    context = resp.getContext();
    if (context == null) {
      return Maps.immutableEntry(null, resp);
    }

    id = Strings.isNullOrEmpty(id) ? (String) context.get("conversation_id") : id;
    contexts.put(id, context);
    resp.setContext(null);
    return Maps.immutableEntry(id, resp);
  }
}
