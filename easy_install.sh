#!/usr/bin/env bash
# install the glazy maven project and launcher script
source .config

# mvn install

ln --symbolic --force "${PWD}/${LAUNCHER}" "${INSTALL_DIR}"
