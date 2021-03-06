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
package org.tpc.codegen.unit.gen.kotlin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import org.tpc.codegen.unit.gen.MethodFilter;
import org.tpc.codegen.unit.gen.Parser;
import org.tpc.codegen.unit.gen.utils.KotlinMappingHelper;
import org.tpc.protocol.core.methods.response.TransactionReceipt;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KotlinParserTest extends Setup {

    @Test
    public void testGenerateJavaPoetStringTypesWhenReturnTypeIsContract() {
        List<Method> listOfFilteredMethods = MethodFilter.extractValidMethods(greeterContractClass);
        Method deploy =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("deploy"))
                        .collect(Collectors.toList())
                        .get(0);
        KotlinParser parser =
                new KotlinParser(greeterContractClass, deploy, new KotlinMappingHelper());
        assertEquals(" %L = %T.deploy(%L, %L, %L, %S).send()", parser.generatePoetStringTypes());
    }

    @Test
    public void testGenerateJavaPoetStringTypesWhenReturnTypeIsNotContract() {

        List<Method> listOfFilteredMethods = MethodFilter.extractValidMethods(greeterContractClass);
        Method newGreeting =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("newGreeting"))
                        .collect(Collectors.toList())
                        .get(0);
        KotlinParser parser =
                new KotlinParser(greeterContractClass, newGreeting, new KotlinMappingHelper());
        assertEquals("val %L = %L.newGreeting(%S).send()", parser.generatePoetStringTypes());
    }

    @Test
    public void testGetMethodReturnType() {
        List<Method> listOfFilteredMethods = MethodFilter.extractValidMethods(greeterContractClass);
        Method newGreeting =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("newGreeting"))
                        .collect(Collectors.toList())
                        .get(0);
        Parser parser =
                new KotlinParser(greeterContractClass, newGreeting, new KotlinMappingHelper());

        assertEquals(TransactionReceipt.class, parser.getMethodReturnType());
    }
}
