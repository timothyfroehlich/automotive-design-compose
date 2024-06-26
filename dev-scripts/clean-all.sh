#!/bin/bash
# Copyright 2023 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Runs all clean jobs that we have, getting the repo roughly back to a clean state

GIT_ROOT=$(git rev-parse --show-toplevel)
./gradlew --stop

(
    cd "$GIT_ROOT/reference-apps/tutorial" || exit
    ./gradlew --init-script ../local-design-compose-repo.init.gradle.kts clean
)
(
    cd "$GIT_ROOT/reference-apps/aaos-unbundled" || exit
    ./gradlew --init-script ../local-design-compose-repo.init.gradle.kts clean
)
(
    cd "$GIT_ROOT" || exit
    ./gradlew clean
    cargo clean
)

(
    cd "$GIT_ROOT/plugins" || exit
    ./gradlew clean
)
(
    cd "$GIT_ROOT/build-logic" || exit
    ./gradlew clean
)

./gradlew --stop
rm -r \
    "$GIT_ROOT/.gradle/configuration-cache" \
    "$GIT_ROOT/plugins/.gradle/configuration-cache" \
    "$GIT_ROOT/build-logic/.gradle/configuration-cache"
