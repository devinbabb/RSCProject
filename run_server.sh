cd /home/rscproject/RSCProject/
cp runserver.log logsbackup/runserver.log.`date +%F.%s`
cp logs/mod.log logsbackup/mod.log.`date +%F`
nohup /home/java/jdk1.6.0_14/bin/java -Xms128m -Xmx2048m -classpath /home/rscproject/RSCProject/lib/mina.jar:/home/rscproject/RSCProject/lib/pircbot.jar:/home/rscproject/RSCProject/lib/bsh.jar:/home/rscproject/RSCProject/lib/xpp3.jar:/home/rscproject/RSCProject/lib/slf4j.jar:/home/rscproject/RSCProject/lib/xstream.jar:/home/rscproject/RSCProject/lib/mysql-connector.jar:/home/rscproject/RSCProject/lib/hex-string.jar:/home/rscproject/RSCProject/conf/server/quests:/home/rscproject/RSCProject/rscd.jar rscproject.gs.Server world.xml p2p yes > runserver.log&

