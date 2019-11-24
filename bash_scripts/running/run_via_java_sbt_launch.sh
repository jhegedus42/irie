/usr/bin/java -Xms1024m -Xmx1024m -XX:ReservedCodeCacheSize=128m -XX:MaxMetaspaceSize=256m -jar /usr/local/Cellar/sbt/1.2.8/libexec/bin/sbt-launch.jar clean
/usr/bin/java -Xms1024m -Xmx1024m -XX:ReservedCodeCacheSize=128m -XX:MaxMetaspaceSize=256m -jar /usr/local/Cellar/sbt/1.2.8/libexec/bin/sbt-launch.jar fastOptJS
/usr/bin/java -Xms1024m -Xmx1024m -XX:ReservedCodeCacheSize=128m -XX:MaxMetaspaceSize=256m -jar /usr/local/Cellar/sbt/1.2.8/libexec/bin/sbt-launch.jar 'server/run 127.0.0.1'

