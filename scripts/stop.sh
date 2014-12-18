#!/bin/sh

pid=$(cat otgserv.pid)
echo "Stopping process \c"
#timer=0
if [ `ps -p $pid | wc -l` -eq 1 ]
then
	echo " not running"
else

	kill -TERM $pid
#################### Тут идет 5-и секундая проверка на то, что процесс завершился, ##########
#################### если нет, его убивают принудительно ####################################
#
#		while [ `ps -p $1 | wc -l` -gt 1 ]
#		do
#			if [ $timer -gt 5 ]
#			then
#				kill -KILL $1
#				timer=0
#			else
#				echo ".\c"
#				sleep 1
#			fi
#			timer=$timer+1
#		done
#
#############################################################################################
	echo " stopped "
fi

rm otgserv.pid
