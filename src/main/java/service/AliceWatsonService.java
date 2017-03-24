/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import com.ibm.watson.developer_cloud.service.WatsonService;

public class AliceWatsonService<T extends WatsonService> {
  protected final T service;

  protected AliceWatsonService(T service, String u, String p, String url) {
    this.service = service;
    this.service.setUsernameAndPassword(u, p);
    this.service.setEndPoint(url);
  }
}
