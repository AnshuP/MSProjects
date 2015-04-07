#!/bin/sh

javac -cp ./jsch-0.1.44.jar:. JSpace/*.java
jar cvf JSpace.jar JSpace/*.class
javac *.java

