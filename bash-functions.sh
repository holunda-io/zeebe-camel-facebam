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
    html=index.html

    echo "<head>" > $html
    echo '<META HTTP-EQUIV="refresh" CONTENT="6">' >> $html
    echo "</head>" >> $html

    echo "<body><h1>Camunda Con - Zeebe Camel Facebam</h1>" >> $html


    for t in $imgDir/*-thumb.jpg
    do
	i=$(echo $t | sed -e "s/-thumb//")
#	i=echo $t | sed -e "s/-thumb//"
	echo "<a href=$i target=_blank><img src=$t /></a>" >> $html
    done

    echo "</body>" >> $html

    echo "updated $html"
}

function zcf.gallery.loop() {
    while true
    do 
	zcf.gallery
	sleep 5
    done
}

