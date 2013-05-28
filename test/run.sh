cd $(dirname $0)
cd ../complete
mvn package
java -jar target/gs-rest-service-complete-0.1.0.jar &
PID=$!
sleep 3
curl -s http://localhost:8080/greeting > target/actual.json
kill -9 $PID

if diff -w ../test/expected.json target/actual.json
	then
		echo SUCCESS
		let ret=0
	else
		echo FAIL
		let ret=255
fi

rm -rf target
exit $ret
