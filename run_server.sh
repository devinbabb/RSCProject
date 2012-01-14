#cd /home/rscproject/RSCProject/
#cp runserver.log logsbackup/runserver.log.`date +%F.%s`
#cp logs/mod.log logsbackup/mod.log.`date +%F`
java -Xms128m -Xmx2048m -classpath /home/devin/RSCProject/lib/mina.jar:/home/devin/RSCProject/lib/pircbot.jar:/home/devin/RSCProject/lib/bsh.jar:/home/devin/RSCProject/lib/xpp3.jar:/home/devin/RSCProject/lib/slf4j.jar:/home/devin/RSCProject/lib/xstream.jar:/home/devin/RSCProject/lib/mysql-connector.jar:/home/devin/RSCProject/lib/hex-string.jar:/home/devin/RSCProject/conf/server/quests:/home/devin/RSCProject/rscd.jar rscproject.gs.Server world.xml p2p yes > runserver.log&

