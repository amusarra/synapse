/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.transport.testkit.tests.async;

import javax.mail.internet.ContentType;

import org.apache.synapse.transport.testkit.client.AsyncTestClient;
import org.apache.synapse.transport.testkit.listener.AsyncChannel;
import org.apache.synapse.transport.testkit.server.AsyncEndpoint;
import org.apache.synapse.transport.testkit.tests.MessageTestCase;

public abstract class AsyncMessageTestCase<M,N> extends MessageTestCase {
    private final AsyncTestClient<M> client;
    private final AsyncEndpoint<N> endpoint;
    
    // TODO: maybe we don't need an explicit AsyncChannel
    public AsyncMessageTestCase(AsyncChannel channel, AsyncTestClient<M> client, AsyncEndpoint<N> endpoint, ContentType contentType, String charset, Object... resources) {
        super(contentType, charset, resources);
        this.client = client;
        this.endpoint = endpoint;
        addResource(channel);
        addResource(client);
        addResource(endpoint);
    }

    @Override
    protected void runTest() throws Throwable {
        M message = prepareMessage();
        
        // Run the test.
        N messageData;
//                    contentTypeMode == ContentTypeMode.TRANSPORT ? contentType : null);
        client.sendMessage(options, options.getBaseContentType(), message);
        messageData = endpoint.waitForMessage(8000).getData();
        if (messageData == null) {
            fail("Failed to get message");
        }
        
        checkMessageData(message, messageData);
    }
    
    protected abstract M prepareMessage() throws Exception;
    protected abstract void checkMessageData(M message, N messageData) throws Exception;
}