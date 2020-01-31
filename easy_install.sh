#!/usr/bin/env bash
# install the glazy maven project and launcher script
source .config

if [ ! -e "${JAR}" ]; then
    mvn clean install
fi

sudo ln --verbose --symbolic --force "${PWD}/${LAUNCHER}" "${INSTALL_DIR}"
