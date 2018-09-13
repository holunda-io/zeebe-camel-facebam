
function zcf.clean() {
# deletes content of _fs folders (except gitignore)
  for fs in `find . -name _fs`
  do
    for f in `find $fs -depth 2 ! -name ".gitignore"`
    do
      echo "deleting: $f"
      rm -rf $f
    done
  done
}

function zcf.deploy() {
  cp docs/image-processing.bpmn cloud/broker/_fs/inbox
}

function zcf.start() {
  cp docs/images/kermit.png cloud/_fs/inbox
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

