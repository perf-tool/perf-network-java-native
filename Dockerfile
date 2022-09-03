#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

FROM perftool/compile:graal AS build
COPY . /opt/perf/compile
WORKDIR /opt/perf/compile
RUN mvn -B package -Dmaven.test.skip=true && \
    mv target/perf-network-java-native-0.0.1-SNAPSHOT-jar-with-dependencies.jar perf-network.jar && \
    native-image -jar perf-network.jar

FROM perftool/base:graal

COPY --from=build /opt/perf/compile/perf-network.jar /opt/perf/perf-network.jar
COPY --from=build /opt/perf/compile/perf-network.build_artifacts.txt /opt/perf/perf-network.build_artifacts.txt
COPY --from=build /opt/perf/compile/perf-network /opt/perf/perf-network

CMD ["/usr/bin/dumb-init", "/opt/perf/perf-network"]
