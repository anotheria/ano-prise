#!/bin/bash
lib_path=lib
dist_path=dist
for file in $(ls $lib_path); do
 lib=$lib:$lib_path/$file
 rmicodebase="$rmicodebase file:$PWD/$lib_path/$file"
done

lib=$lib:dist/cdate-biz.jar
#rmicodebase="file:$PWD/dist/distributeme.jar $rmicodebase"
rmicodebase="file:$PWD/dist/ano-prise.jar $rmicodebase"
CLASSPATH=dist/ano-prise.jar:etc/appdata:$lib
echo CLASSPATH: $CLASSPATH
echo rmicodebase=$rmicodebase

#defaultEnvironment = dev ? Choose yours
java -Xmx700M -Xms64M -classpath $CLASSPATH -Djava.rmi.server.codebase="$rmicodebase" -Dconfigureme.defaultEnvironment=dev $*
