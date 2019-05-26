#!/usr/bin/env bash

current_path=`pwd`
work_dir=`dirname $0`
work_dir=`echo "$current_path/$work_dir"`
host="ec2-user@ec2-52-69-97-31.ap-northeast-1.compute.amazonaws.com"
cd "$work_dir"
echo " Working Directory: $work_dir"

case "$1" in
  "fmt")
    sbt scalafmt
    ;;
  "pbLocal")
    sbt publishLocal
    ;;
  "pb")
    sbt publish
    ;;
  *)
    sbt clean compile
    ;;
esac
