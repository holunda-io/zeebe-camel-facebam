#!/usr/bin/env bash

function zcf.clean() {
# deletes content of _fs folders (except gitignore)
  for fs in `find . -name _demo`
  do
    echo $fs
  done
}

function zcf.deploy() {
  cp docs/image-processing.bpmn _demo/cloud/_application/orchestrator
}

function zcf.start() {
  cp docs/images/kermit.png _demo/cloud/_application/orchestrator
}

function zcf.list() {
  for fs in `find . -name _fs`
  do
    for f in `find $fs -depth 1`
    do
      echo "--------------- : $f"
      ls -l1 $f
    done
  done
}

function zcf.refresh() {
  source bash-functions.sh
}

