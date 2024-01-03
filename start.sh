echo "PID Check..."

CURRENT_PID=$(ps -ef | grep java | grep e-contract-1.*-RELEASE | awk '{print $2}')

echo "Running PID: {$CURRENT_PID}"

if [ -z "$CURRENT_PID" ] ; then
        echo "Project is not running"
else
        kill -9 $CURRENT_PID
        sleep 10
fi

echo "Deploy Electronic Contract Project...."

# TODO: 서버 세팅 후 경로 변경
cd /path/to/deploy/project

JAR_FILE=$(find ./build/libs/ -type f -iname "e-contract-1.*-RELEASE.jar")
#echo $JAR_FILE

#nohup java -jar -Dspring.profiles.active=dev -Dproperties.jasypt.encryptor.password='dejay1234!@#$' ./build/libs/framework-1.*.*.RELEASE.jar >> ./logs/framework.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev -Dproperties.jasypt.encryptor.password='dejay1234!@#$' $JAR_FILE >> ./logs/contract.log 2>&1 &

echo "Done"