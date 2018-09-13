#!/usr/bin/env bash

for fs in `find . -name _fs`
do
  for f in `find $fs -depth 2 ! -name ".gitignore"`
  do
    echo
  done
done

