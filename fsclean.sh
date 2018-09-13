#!/usr/bin/env bash

# deletes content of _fs folders (except gitignore)
for fs in `find . -name _fs`
do
  for f in `find $fs -depth 2 ! -name ".gitignore"`
  do
    echo $f
    rm -rf $f
  done
done

