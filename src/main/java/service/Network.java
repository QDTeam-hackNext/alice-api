/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.http.HttpHeaders;
import com.ibm.watson.developer_cloud.http.HttpMediaType;

import okhttp3.Call;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Network {
  class Result<U> {
    final U value;
    final boolean ok;

    Result(U value, boolean ok) {
      this.value = value;
      this.ok = ok;
    }
  }

  private final OkHttpClient client;
  private final Gson gson;

  Network() {
    this.client = client();
    this.gson = new Gson();
  }

  <T, U> Result<U> post(String url, T input, Type type) {
    RequestBuilder builder = RequestBuilder.post(url);
    if (input != null) {
      builder.withJsonBody(gson.toJson(input));
    }
    return performRequest(builder.build(), type);
  }

  <U> Result<U> performRequest(Request req, Type type) {
    Call call = client.newCall(req);
    try {
      Response response = call.execute();
      if (response.isSuccessful()) {
        try (Reader data = response.body().charStream()) {
          U value = gson.fromJson(data, type);
          return new Result<U>(value, true);
        }
      }
      throw new RuntimeException(
          String.format("Call to %s failed with status:%d and result %s", req.url(), response.code(), response.body()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private OkHttpClient client() {
    CookieManager mgr = new CookieManager();
    mgr.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

    return new OkHttpClient.Builder()
      .cookieJar(new JavaNetCookieJar(mgr))
      .connectTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
      .readTimeout(90, TimeUnit.SECONDS)
      .build();
  }

  static class RequestBuilder {
    enum Method {POST, GET}

    private final Method method;
    private final String url;

    private RequestBody body;

    private RequestBuilder(Method method, String url) {
      this.method = method;
      this.url = url;
    }

    static RequestBuilder post(String url) {
      return new RequestBuilder(Method.POST, url);
    }

    static RequestBuilder get(String url) {
      return new RequestBuilder(Method.GET, url);
    }

    RequestBuilder withJsonBody(String body) {
      this.body = RequestBody.create(MediaType.parse(HttpMediaType.APPLICATION_JSON), body);
      return this;
    }

    Request build() {
      Request.Builder builder = new Request.Builder();
      builder.url(url);
      if (body == null && Method.POST == method) {
        body = RequestBody.create(null, new byte[0]);
      }
      builder.method(method.name(), body);
      builder.header(HttpHeaders.ACCEPT, HttpMediaType.APPLICATION_JSON);
      return builder.build();
    }
  }
}
