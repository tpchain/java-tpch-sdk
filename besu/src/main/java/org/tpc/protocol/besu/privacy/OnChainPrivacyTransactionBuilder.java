/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.tpc.protocol.besu.privacy;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tpc.abi.FunctionEncoder;
import org.tpc.abi.Utils;
import org.tpc.abi.datatypes.DynamicArray;
import org.tpc.abi.datatypes.Function;
import org.tpc.abi.datatypes.generated.Bytes32;
import org.tpc.crypto.Credentials;
import org.tpc.protocol.eea.crypto.PrivateTransactionEncoder;
import org.tpc.protocol.eea.crypto.RawPrivateTransaction;
import org.tpc.tx.gas.BesuPrivacyGasProvider;
import org.tpc.utils.Base64String;
import org.tpc.utils.Numeric;
import org.tpc.utils.Restriction;

public class OnChainPrivacyTransactionBuilder {

    private static final BesuPrivacyGasProvider ZERO_GAS_PROVIDER =
            new BesuPrivacyGasProvider(BigInteger.valueOf(0));

    public static String getEncodedRemoveFromGroupFunction(
            Base64String enclaveKey, byte[] participant) {
        final org.tpc.abi.datatypes.Function function =
                new org.tpc.abi.datatypes.Function(
                        "removeParticipant",
                        Arrays.asList(new Bytes32(enclaveKey.raw()), new Bytes32(participant)),
                        Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    public static String getEncodedAddToGroupFunction(
            Base64String enclaveKey, List<byte[]> participants) {
        final Function function =
                new Function(
                        "addParticipants",
                        Arrays.asList(
                                new Bytes32(enclaveKey.raw()),
                                new DynamicArray<>(
                                        Bytes32.class, Utils.typeMap(participants, Bytes32.class))),
                        Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    public static String getEncodedSingleParamFunction(final String functionName) {
        final Function function =
                new Function(functionName, Collections.emptyList(), Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    public static String buildOnChainPrivateTransaction(
            Base64String privacyGroupId,
            Credentials credentials,
            Base64String enclaveKey,
            final BigInteger nonce,
            String call) {

        RawPrivateTransaction rawTransaction =
                RawPrivateTransaction.createTransaction(
                        nonce,
                        ZERO_GAS_PROVIDER.getGasPrice(),
                        ZERO_GAS_PROVIDER.getGasLimit(),
                        "0x000000000000000000000000000000000000007c",
                        call,
                        enclaveKey,
                        privacyGroupId,
                        Restriction.RESTRICTED);

        return Numeric.toHexString(
                PrivateTransactionEncoder.signMessage(rawTransaction, 2018, credentials));
    }
}
