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
package org.tpc.codegen;

import org.slf4j.Logger;

/** A reporter generation that outputs messages using a logger instance. */
class LogGenerationReporter implements GenerationReporter {

    private final Logger logger;

    public LogGenerationReporter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void report(String msg) {
        logger.warn(msg);
    }
}
