#!/usr/bin/env bash

function zcf.deploy() {
  cp docs/image-processing.bpmn _demo/cloud/_application/orchestrator
}

function zcf.start.png() {
  cp docs/images/kermit.png _demo/cloud/_application/orchestrator
}
function zcf.start.jpg() {
  cp docs/images/IMG_0612.jpg _demo/cloud/_application/orchestrator
}

function zcf.list() {
  declare -a demo_dirs=("client/inbox" "client/outbox" "cloud/inbox" "cloud/outbox"  "cloud/_application/_work"  "cloud/_application/orchestrator" "cloud/_application/watermarker" "cloud/_application/thumbnailer")
  for d in "${demo_dirs[@]}"

  #for d in `find _demo ! -name ".gitignore" ! -name ".DS_Store"`
  do
    echo "--------- $d"
    find "_demo/$d" -type f ! -name ".gitignore" ! -name ".DS_Store"
  done
}

function zcf.clean() {
  find _demo -type d -name ".camel" -print -exec rm -rf {} \;

  find _demo -type f -name "*.png" -print -delete
  find _demo -type f -name "*.jpg" -print -delete
  find _demo -type f -name "*.job" -print -delete
  find _demo -type f -name "*.complete" -print -delete
  find _demo -type f -name "*.register" -print -delete

  zcf.list
}

function zcf.refresh() {
  source bash-functions.sh
}

