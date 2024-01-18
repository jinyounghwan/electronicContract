echo ">> START DEPLOYING APPLICATION <<"

PROJECT_REPO=/home/app-test
JAR_FILE=e-contract.jar

chmod -R 775 $PROJECT_REPO

echo ">> CURRENT_PID Checking... <<"
CURRENT_PID=$(pgrep -f $JAR_FILE)

echo ">> Running PID: {$CURRENT_PID}"

if [ -z "$CURRENT_PID" ] ; then
        echo ">> Project is not running"
else
        kill -9 $CURRENT_PID
        sleep 10
fi

echo ">> Deploying Electronic Contract Project..."

# TODO: 서버 세팅 후 경로 변경
cd /home/app-test

nohup java -jar -Dspring.profiles.active=dev -Dproperties.jasypt.encryptor.password='dejay1234!@#$' $JAR_FILE >> ./logs/contract.log 2>&1 &

echo "Done"
