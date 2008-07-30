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

package org.apache.synapse.transport.vfs;

import java.io.File;

import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.synapse.transport.testkit.listener.AbstractChannel;
import org.apache.synapse.transport.testkit.listener.AsyncChannel;
import org.apache.synapse.transport.vfs.VFSTransportListenerTest.TestStrategyImpl;

public class VFSFileChannel extends AbstractChannel<VFSTransportListenerTest.TestStrategyImpl> implements AsyncChannel<VFSTransportListenerTest.TestStrategyImpl> {
    private final File requestFile;
    
    public VFSFileChannel(TestStrategyImpl setup, File requestFile) {
        super(setup);
        this.requestFile = requestFile;
    }

    public File getRequestFile() {
        return requestFile;
    }

    public TransportInDescription createTransportInDescription() {
        TransportInDescription trpInDesc =
            new TransportInDescription(VFSTransportListener.TRANSPORT_NAME);
        trpInDesc.setReceiver(new VFSTransportListener());
        return trpInDesc;
    }
    
    @Override
    public TransportOutDescription createTransportOutDescription() throws Exception {
        TransportOutDescription trpOutDesc =
            new TransportOutDescription(VFSTransportSender.TRANSPORT_NAME);
        trpOutDesc.setSender(new VFSTransportSender());
        return trpOutDesc;
    }
    
    @Override
    public void setupService(AxisService service) throws Exception {
        service.addParameter("transport.vfs.FileURI", "vfs:" + requestFile.toURL());
        service.addParameter("transport.PollInterval", "1");
        service.addParameter("transport.vfs.ActionAfterProcess", "DELETE");
    }

    @Override
    public void setUp() throws Exception {
        requestFile.getParentFile().mkdirs();
        requestFile.delete();
    }
}