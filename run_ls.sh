cp runls.log logsbackup/runls.log.`date +%F.%s`
nohup /home/java/jdk1.6.0_14/bin/java -Xms56m -Xmx128m -classpath /home/rscproject/RSCProject/lib/mina.jar:/home/rscproject/RSCProject/lib/xpp3.jar:/home/rscproject/RSCProject/lib/slf4j.jar:/home/rscproject/RSCProject/lib/xstream.jar:/home/rscproject/RSCProject/lib/mysql-connector.jar:/home/rscproject/RSCProject/lib/hex-string.jar:/home/rscproject/RSCProject/rscd.jar rscproject.ls.Server ls.conf > runls.log&

