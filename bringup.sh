#!/bin/bash
sudo killall java
sudo java -Djava.library.path=/usr/lib/rxtx:/usr/lib/jni -jar /home/pi/C2NET/nodec2net/target/IoT_node-0.1.1-SNAPSHOT-jar-with-dependencies.jar
