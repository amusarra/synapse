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

package org.apache.synapse.transport.testkit.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.synapse.transport.testkit.TransportTestSuite;
import org.apache.synapse.transport.testkit.tests.TransportTestCase;

public class LogManager {
    public static final LogManager INSTANCE = new LogManager();
    
    private final File logDir;
    private File testSuiteDir;
    private File testCaseDir;
    private int sequence;
    
    private LogManager() {
        logDir = new File("target" + File.separator + "testkit-logs");
    }
    
    public void setTestSuite(TransportTestSuite suite) {
        if (suite == null) {
            testSuiteDir = null;
        } else {
            testSuiteDir = new File(logDir, suite.getTestClass().getName());
        }
        testCaseDir = null;
    }
    
    public void setTestCase(TransportTestCase testCase) {
        if (testCase == null) {
            testCaseDir = null;
        } else {
            testCaseDir = new File(testSuiteDir, testCase.getId());
            sequence = 1;
        }
    }
    
    public OutputStream createLog(String name) throws IOException {
        testCaseDir.mkdirs();
        return new FileOutputStream(new File(testCaseDir, StringUtils.leftPad(String.valueOf(sequence++), 2, '0') + "-" + name + ".log"));
    }
}