/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.perftool.network;

import com.perftool.network.constant.PerfnConst;
import com.perftool.network.util.EnvUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {

    public static void main(String[] args) throws Exception {
        log.info("perf network start");
        String envProtocolType = System.getenv(PerfnConst.CONF_PROTOCOL_TYPE);
        String envCommType = System.getenv(PerfnConst.CONF_COMM_TYPE);
        boolean prometheusMetricsDisable = EnvUtil.getBoolean("PROMETHEUS_METRICS_DISABLE", false);
        Perfn.start(envProtocolType, envCommType, prometheusMetricsDisable);
    }

}
