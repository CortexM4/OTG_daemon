#!/bin/sh
echo "Starting process \c"

# Run process in background with closed input stream, to detach it from terminal
java -jar ../dist/OTGService.jar <&- 2>otgserv.log &
#Write pid to pid file
echo $! > otgserv.pid
if [ ${#!} -eq 0 ]
then
	echo "... [ failed ]"
else 
	echo "... [ Accepted ]"
fi
