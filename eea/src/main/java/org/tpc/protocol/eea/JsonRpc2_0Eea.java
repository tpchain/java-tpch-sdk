/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.tpc.protocol.eea;

import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import org.tpc.protocol.Web3jService;
import org.tpc.protocol.core.JsonRpc2_0Web3j;
import org.tpc.protocol.core.Request;
import org.tpc.protocol.core.methods.response.EthSendTransaction;

public class JsonRpc2_0Eea extends JsonRpc2_0Web3j implements Eea {
    public JsonRpc2_0Eea(Web3jService web3jService) {
        super(web3jService);
    }

    public JsonRpc2_0Eea(
            Web3jService web3jService,
            long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        super(web3jService, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, EthSendTransaction> eeaSendRawTransaction(
            final String signedTransactionData) {
        return new Request<>(
                "eea_sendRawTransaction",
                Collections.singletonList(signedTransactionData),
                web3jService,
                EthSendTransaction.class);
    }
}
