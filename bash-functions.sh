#!/usr/bin/env bash

function zcf.deploy() {
  cp docs/image-processing.bpmn _demo/cloud/_application/orchestrator
}

function zcf.start.png() {
  cp docs/images/kermit.png _demo/cloud/inbox
}
function zcf.start.jpg() {
  cp docs/images/IMG_0612.jpg _demo/cloud/inbox
}

function zcf.list() {
  tree -a -I ".gitignore|.DS_Store" _demo/
}

function zcf.clean() {
  find _demo -type d -name ".camel" -print -exec rm -rf {} \;

  find _demo -type f -name "*.png" -print -delete
  find _demo -type f -name "*.jpg" -print -delete
  find _demo -type f -name "*.bpmn" -print -delete
  find _demo -type f -name "*.job" -print -delete
  find _demo -type f -name "*.complete" -print -delete
  find _demo -type f -name "*.register" -print -delete

  zcf.list
}

function zcf.refresh() {
  source bash-functions.sh
}

