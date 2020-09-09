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
package org.tpc.utils;

import java.io.IOException;

import org.tpc.protocol.Web3j;
import org.tpc.protocol.core.DefaultBlockParameter;
import org.tpc.protocol.core.methods.request.Transaction;
import org.tpc.protocol.core.methods.response.TransactionReceipt;

/** Revert reason extraction and retrieval functions. */
public class RevertReasonExtractor {

    public static final String MISSING_REASON = "N/A";

    /**
     * Extracts the error reason of a reverted transaction (if one exists and enabled).
     *
     * @param transactionReceipt the reverted transaction receipt
     * @param data the reverted transaction data
     * @param web3j Web3j instance
     * @param revertReasonCallEnabled flag of reason retrieval via additional call
     * @return the reverted transaction error reason if exists or null otherwise
     * @throws IOException if the call to the node fails
     */
    public static String extractRevertReason(
            TransactionReceipt transactionReceipt,
            String data,
            Web3j web3j,
            Boolean revertReasonCallEnabled)
            throws IOException {

        if (transactionReceipt.getRevertReason() != null) {
            return transactionReceipt.getRevertReason();
        } else if (revertReasonCallEnabled) {
            String revertReason = retrieveRevertReason(transactionReceipt, data, web3j);
            if (revertReason != null) {
                transactionReceipt.setRevertReason(revertReason);
                return revertReason;
            }
        }
        return MISSING_REASON;
    }

    /**
     * Retrieves the error reason of a reverted transaction (if one exists).
     *
     * @param transactionReceipt the reverted transaction receipt
     * @param data the reverted transaction data
     * @param web3j Web3j instance
     * @return the reverted transaction error reason if exists or null otherwise
     * @throws IOException if the call to the node fails
     */
    public static String retrieveRevertReason(
            TransactionReceipt transactionReceipt, String data, Web3j web3j) throws IOException {

        if (transactionReceipt.getBlockNumber() == null) {
            return null;
        }
        return web3j.ethCall(
                        Transaction.createEthCallTransaction(
                                transactionReceipt.getFrom(), transactionReceipt.getTo(), data),
                        DefaultBlockParameter.valueOf(transactionReceipt.getBlockNumber()))
                .send()
                .getRevertReason();
    }
}
