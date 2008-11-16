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
package org.apache.synapse.commons.util.datasource.factory;

import org.apache.synapse.commons.util.datasource.DataSourceInformation;
import org.apache.synapse.commons.util.datasource.DataSourceInformationRepository;
import org.apache.synapse.commons.util.datasource.DataSourceInformationRepositoryListener;
import org.apache.synapse.commons.util.datasource.DataSourceManager;

import java.util.List;
import java.util.Properties;

/**
 *
 */
public class DataSourceInformationRepositoryFactory {

    public static DataSourceInformationRepository createDataSourceInformationRepository(Properties properties) {

        return createDataSourceInformationRepository(properties, DataSourceManager.getInstance());
    }

    public static DataSourceInformationRepository createDataSourceInformationRepository(Properties properties, DataSourceInformationRepositoryListener listener) {

        List<DataSourceInformation> dataSourceInformations = DataSourceInformationListFactory.createDataSourceInformationList(properties);
        DataSourceInformationRepository repository = new DataSourceInformationRepository();
        repository.registerDataSourceInformationRepositoryListener(listener);
        repository.setConfigurationProperties(properties);
        for (DataSourceInformation information : dataSourceInformations) {
            if (information != null) {
                repository.addDataSourceInformation(information);
            }
        }
        return repository;
    }
}