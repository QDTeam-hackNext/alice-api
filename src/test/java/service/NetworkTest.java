/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.watson.developer_cloud.http.HttpMediaType;

import okhttp3.HttpUrl;
import okhttp3.Request;
import service.Network.RequestBuilder;

public class NetworkTest {
  private static final String URL = "http://google.com";

  @Test
  public void testRequestContent() throws Exception {
    // given
    String jsonBody = "{\"test\":\"value\"}";
    RequestBuilder objectUnderTest = Network.RequestBuilder.post(URL)
      .withJsonBody(jsonBody);

    // when
    Request actual = objectUnderTest.build();

    // then
    assertEquals(HttpUrl.parse(URL), actual.url());
    assertTrue(actual.body().contentType().toString().contains(HttpMediaType.APPLICATION_JSON));
  }

  @Test
  public void testRequestPostMethod() throws Exception {
    // given
    RequestBuilder objectUnderTest = Network.RequestBuilder.post(URL);

    // when
    Request actual = objectUnderTest.build();

    // then
    assertEquals(Network.RequestBuilder.Method.POST.name(), actual.method());
  }

  @Test
  public void testRequestGetMethod() throws Exception {
    // given
    RequestBuilder objectUnderTest = Network.RequestBuilder.get(URL);

    // when
    Request actual = objectUnderTest.build();

    // then
    assertEquals(Network.RequestBuilder.Method.GET.name(), actual.method());
  }
}
