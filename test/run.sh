cd $(dirname $0)
cd ../initial

mvn clean compile
ret=$?
if [ $ret -ne 0 ]; then
exit $ret
fi
rm -rf target

./gradlew compileJava
ret=$?
if [ $ret -ne 0 ]; then
exit $ret
fi
rm -rf build

cd ../complete
mvn clean package
java -jar target/gs-rest-service-0.1.0.jar &
PID=$!
sleep 10
curl -s http://localhost:8080/greeting > target/actual.json
kill -9 $PID

echo "Let's look at the actual results: `cat target/actual.json`"
echo "And compare it to: `cat ../test/expected.json`"

if diff -w ../test/expected.json target/actual.json
    then
        echo SUCCESS
        let ret=0
    else
        echo FAIL
        let ret=255
        exit $ret
fi

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; 
  then 
    curl https://raw.githubusercontent.com/timkay/aws/master/aws -o aws
    chmod u+x aws
    ./aws put --progress "x-amz-acl: public-read" springio-guides/gs-rest-service-0.1.0.jar target/gs-rest-service-0.1.0.jar
fi

rm -rf target

./gradlew build
ret=$?
if [ $ret -ne 0 ]; then
exit $ret
fi

rm -rf build
exit
