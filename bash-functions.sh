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



function zcf.gallery() {
    imgDir=_demo/cloud/_application/_work

    echo '<html>' > index.html
    echo '<head><link rel="stylesheet" type="text/css" href="docs/fancy/jquery.fancybox.min.css">' >> index.html
    echo '<meta http-equiv="refresh" content="17">' >> index.html
    echo '</head><body>' >> index.html
    echo '<h1>Zeebe Camel Facebam</h1>' >> index.html
    for thumb in $imgDir/*-thumb.jpg
    do
      img=$(echo $thumb | sed -e "s/-thumb//")
      echo "\n<a href=\"$img\" data-fancybox><img src=\"$thumb\" /></a>" >> index.html
    done

    echo '<script src="docs/fancy/jquery.min.js"></script>' >> index.html
	  echo '<script src="docs/fancy/jquery.fancybox.min.js"></script>' >> index.html
	  echo '</body></html>' >> index.html
}

function zcf.gallery.loop() {
    while true
    do 
	zcf.gallery
	sleep 9
    done
}

