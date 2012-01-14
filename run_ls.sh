#cp runls.log logsbackup/runls.log.`date +%F.%s`
java -Xms56m -Xmx128m -classpath /home/devin/RSCProject/lib/mina.jar:/home/devin/RSCProject/lib/xpp3.jar:/home/devin/RSCProject/lib/slf4j.jar:/home/devin/RSCProject/lib/xstream.jar:/home/devin/RSCProject/lib/mysql-connector.jar:/home/devin/RSCProject/lib/hex-string.jar:rscd.jar rscproject.ls.Server ls.conf > runls.log&

