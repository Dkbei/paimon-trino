/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.paimon.trino;

import io.trino.spi.connector.Connector;
import io.trino.spi.connector.ConnectorTransactionHandle;
import io.trino.spi.transaction.IsolationLevel;

import static io.trino.spi.transaction.IsolationLevel.READ_COMMITTED;
import static io.trino.spi.transaction.IsolationLevel.checkConnectorSupports;
import static java.util.Objects.requireNonNull;

/** Trino {@link Connector}. */
public class TrinoConnector implements Connector {
    private final TrinoMetadataBase trinoMetadata;
    private final TrinoSplitManagerBase trinoSplitManager;
    private final TrinoPageSourceProvider trinoPageSourceProvider;

    public TrinoConnector(
            TrinoMetadataBase trinoMetadata,
            TrinoSplitManagerBase trinoSplitManager,
            TrinoPageSourceProvider trinoPageSourceProvider) {
        this.trinoMetadata = requireNonNull(trinoMetadata, "jmxMetadata is null");
        this.trinoSplitManager = requireNonNull(trinoSplitManager, "jmxSplitManager is null");
        this.trinoPageSourceProvider =
                requireNonNull(trinoPageSourceProvider, "jmxRecordSetProvider is null");
    }

    @Override
    public ConnectorTransactionHandle beginTransaction(
            IsolationLevel isolationLevel, boolean readOnly) {
        checkConnectorSupports(READ_COMMITTED, isolationLevel);
        return TrinoTransactionHandle.INSTANCE;
    }

    @Override
    public TrinoMetadataBase getMetadata(ConnectorTransactionHandle transactionHandle) {
        return trinoMetadata;
    }

    @Override
    public TrinoSplitManagerBase getSplitManager() {
        return trinoSplitManager;
    }

    @Override
    public TrinoPageSourceProvider getPageSourceProvider() {
        return trinoPageSourceProvider;
    }
}
