iptables -D rscproject -j DROP
while [ 1 == 1 ]
do
echo blocking
iptables -I rscproject -j DROP
sleep 60
echo unblocking
iptables -D rscproject -j DROP
sleep 30
done
