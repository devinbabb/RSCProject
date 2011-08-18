        IP_BAN_NOW=0
        cat nullips | while read line; do
                CURR_LINE_CONN=$(echo $line | cut -d" " -f1)
                CURR_LINE_IP=$(echo $line | cut -d" " -f2)
                if [ $CURR_LINE_CONN -lt 20 ]; then
                        break
                fi
                IP_BAN_NOW=1
                echo "$CURR_LINE_IP with $CURR_LINE_CONN connections"
                        route delete $CURR_LINE_IP
                        route add $CURR_LINE_IP gw 127.0.0.1
                        #$IPT -I ddos -s $CURR_LINE_IP -j DROP
	done
