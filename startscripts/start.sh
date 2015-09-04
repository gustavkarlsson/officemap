#!/bin/bash
BASEDIR=$(dirname $0)

cd $BASEDIR
bin/server db migrate && bin/server server
