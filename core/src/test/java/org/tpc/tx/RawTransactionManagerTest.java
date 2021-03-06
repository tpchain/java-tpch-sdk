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
package org.tpc.tx;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import org.tpc.crypto.SampleKeys;
import org.tpc.protocol.core.methods.response.TransactionReceipt;
import org.tpc.tx.exceptions.TxHashMismatchException;
import org.tpc.utils.Convert;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RawTransactionManagerTest extends ManagedTransactionTester {

    @Test
    public void testTxHashMismatch() throws IOException {
        TransactionReceipt transactionReceipt = prepareTransfer();
        prepareTransaction(transactionReceipt);

        TransactionManager transactionManager =
                new RawTransactionManager(web3j, SampleKeys.CREDENTIALS);
        Transfer transfer = new Transfer(web3j, transactionManager);
        assertThrows(
                TxHashMismatchException.class,
                () -> transfer.sendFunds(ADDRESS, BigDecimal.ONE, Convert.Unit.ETHER).send());
    }
}
