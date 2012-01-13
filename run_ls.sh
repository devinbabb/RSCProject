#cp runls.log logsbackup/runls.log.`date +%F.%s`
nohup java -Xms56m -Xmx128m -classpath /home/devin/Projects/RSCProject/lib/mina.jar:/home/devin/Projects/RSCProject/lib/xpp3.jar:/home/devin/Projects/RSCProject/lib/slf4j.jar:/home/devin/Projects/RSCProject/lib/xstream.jar:/home/devin/Projects/RSCProject/lib/mysql-connector.jar:/home/devin/Projects/RSCProject/lib/hex-string.jar:rscd.jar rscproject.ls.Server ls.conf > runls.log&

