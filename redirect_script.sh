while [ 1 == 1 ]; do
cat runserver.log|grep Anti-Bot > /home/rscproject/public_html/lolbans.txt
chmod 777 /home/rscproject/public_html/lolbans.txt
sleep 30
done
