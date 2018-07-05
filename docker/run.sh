#!/bin/bash

set -e

PARAMS=""

while [ $# -gt 0 ]
do
    key="$1"

    case $key in
        -ENVIRONMENT_NAME \
        |-ELASTICSEARCH_HOST \
        |-ENVIRONMENT_NAME \
        |-PROJECT \
        |-APP_PORT \
        |-INFLUXDB_HOST \
        |-INFLUXDB_USERNAME \
        |-INFLUXDB_PASSWORD \
        |-VERBOSE)

            key=${key:1}    # Removing first character
            declare "$key"="$2"
            shift # past argument
        ;;
        --*) # Filtering blank Spring parameters

            IFS='=' read -ra PARAM <<< "$1"
            if [ -n "${PARAM[1]}" ]; then
                PARAMS="$PARAMS $1"
            fi

        ;;
        *)
        # unknown option
        ;;
    esac
shift # past argument or value
done

if [ "$VERBOSE" == "true" ]; then
    echo $@
    echo "PARAMS: $PARAMS"
    echo "ENVIRONMENT_NAME: ${ENVIRONMENT_NAME}"
    echo "PROJECT: ${PROJECT}"
    echo "ELASTICSEARCH_HOST: ${ELASTICSEARCH_HOST}"
    echo "APP_PORT: ${APP_PORT}"
    echo "INFLUXDB_HOST: ${INFLUXDB_HOST}"
    echo "INFLUXDB_USERNAME: ${INFLUXDB_USERNAME}"
    echo "INFLUXDB_PASSWORD: ${INFLUXDB_PASSWORD}"
    echo java -jar $PROJECT.jar $PARAMS
fi

# Logs through filebeat
sed -i "s/ENVIRONMENT_NAME/$ENVIRONMENT_NAME/g" filebeat.yml
sed -i "s/PROJECT/$PROJECT/g" filebeat.yml
sed -i "s/ELASTICSEARCH_HOST/$ELASTICSEARCH_HOST/g" filebeat.yml
cp filebeat.yml /etc/filebeat/filebeat.yml
service filebeat start

# Metrics through telegraf
sed -i "s/ENVIRONMENT_NAME/$ENVIRONMENT_NAME/g" telegraf.conf
sed -i "s/PROJECT/$PROJECT/g" telegraf.conf
sed -i "s/APP_PORT/$APP_PORT/g" telegraf.conf
INFLUXDB_HOST_ESCAPED=$(echo $INFLUXDB_HOST | sed 's/\//\\\//g')
sed -i "s/INFLUXDB_HOST/$INFLUXDB_HOST_ESCAPED/g" telegraf.conf
sed -i "s/INFLUXDB_USERNAME/$INFLUXDB_USERNAME/g" telegraf.conf
sed -i "s/INFLUXDB_PASSWORD/$INFLUXDB_PASSWORD/g" telegraf.conf
cp telegraf.conf /etc/telegraf/telegraf.conf
service telegraf start

java -jar widget-action-0.0.1-SNAPSHOT.jar $PARAMS
