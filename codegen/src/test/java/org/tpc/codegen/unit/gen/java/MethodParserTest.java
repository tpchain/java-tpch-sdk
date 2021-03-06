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
package org.tpc.codegen.unit.gen.java;

import java.lang.reflect.Method;
import java.util.Optional;

import com.squareup.javapoet.MethodSpec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodParserTest extends Setup {
    @Test
    public void testThatDeployMethodWasGenerated() {

        Optional<Method> deployMethod =
                filteredMethods.stream().filter(m -> m.getName().equals("deploy")).findAny();
        MethodSpec deployMethodSpec =
                new MethodParser(deployMethod.get(), greeterContractClass).getMethodSpec();
        assertEquals(
                "@org.junit.jupiter.api.BeforeAll\n"
                        + "static void deploy(org.tpc.protocol.Web3j web3j, org.tpc.tx.TransactionManager transactionManager, org.tpc.tx.gas.ContractGasProvider contractGasProvider) throws java.lang.Exception {\n"
                        + "  greeter = org.com.test.contract.Greeter.deploy(web3j, transactionManager, contractGasProvider, \"REPLACE_ME\").send();\n"
                        + "}\n",
                deployMethodSpec.toString());
    }

    @Test
    public void testThatNewGreetingMethodWasGenerated() {

        Optional<Method> deployMethod =
                filteredMethods.stream().filter(m -> m.getName().equals("newGreeting")).findAny();
        MethodSpec deployMethodSpec =
                new MethodParser(deployMethod.get(), greeterContractClass).getMethodSpec();
        assertEquals(
                "org.tpc.protocol.core.methods.response.TransactionReceipt transactionReceiptVar = greeter.newGreeting(\"REPLACE_ME\").send();\n"
                        + "org.junit.jupiter.api.Assertions.assertTrue(transactionReceiptVar.isStatusOK());\n",
                deployMethodSpec.code.toString());
    }
}
