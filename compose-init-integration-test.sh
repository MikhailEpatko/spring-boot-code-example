#!/bin/sh

set -e
SCRIPT_NAME=${0##*/}
INIT_DB_TIMEOUT=120s

# Init db
echo "$(date +'%Y.%m.%d %T') - [INFO ${SCRIPT_NAME}] Init DB, timeout ${INIT_DB_TIMEOUT}"
timeout $INIT_DB_TIMEOUT src/test/resources/init-db.sh

# Run integration tests
echo "$(date +'%Y.%m.%d %T') - [INFO ${SCRIPT_NAME}] Running integration tests"
mvn clean test -Dmaven.repo.local=/cache/.m2/repository \
        -Dgroups=integration \
        -Dserver-port=88889 \
