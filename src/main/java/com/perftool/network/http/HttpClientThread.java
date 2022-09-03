/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.perftool.network.http;

import com.google.common.util.concurrent.RateLimiter;
import com.perftool.network.config.ClientConfig;
import com.perftool.network.util.RandomUtil;
import lombok.extern.log4j.Log4j2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j2
public class HttpClientThread extends Thread {

    private final ClientConfig clientConfig;

    private final HttpClient httpClient;

    private final RateLimiter rateLimiter;

    public HttpClientThread(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.httpClient = HttpClient.newBuilder().build();
        this.rateLimiter = RateLimiter.create(clientConfig.getTickPerConnMs() / 1000);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            rateLimiter.acquire();
            String uri = String.format("http://%s:%d/%s", clientConfig.getHost(), clientConfig.getPort(), "perf");
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(
                    RandomUtil.randomBytes(clientConfig.getPacketSize()));
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(uri)).POST(bodyPublisher).build();
            try {
                this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                log.error("send http request fail ", e);
                break;
            }
        }
    }
}
