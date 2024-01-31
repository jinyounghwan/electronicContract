echo ">> START DEPLOYING APPLICATION <<"

# TODO: 서버 세팅 후 경로 변경
PROJECT_REPO=/home/app
JAR_FILE=$(find ./ -type f -iname 'e-contract*-RELEASE.jar')

chmod -R 775 $PROJECT_REPO

echo ">> CURRENT_PID Checking... <<"
CURRENT_PID=$(pgrep -f $JAR_FILE)

echo ">> Running PID: {$CURRENT_PID} <<"

if [ -z "$CURRENT_PID" ] ; then
        echo ">> Project is not running"
else
        kill -9 $CURRENT_PID
        sleep 10
fi

echo ">> Deploying Electronic Contract Application...! <<"
# TODO: 서버 세팅 후 경로 변경
cd $PROJECT_REPO
echo ">> PWD: {$PWD} <<"

#nohup java -jar -Dspring.profiles.active=dev -Dproperties.jasypt.encryptor.password='dejay1234!@#$' $JAR_FILE >> ./logs/contract.log 2>&1 &
nohup java -jar -Dspring.profiles.active=prod -Dproperties.jasypt.encryptor.password='dejay1234!@#$' $JAR_FILE > /dev/null &

echo "DEPLOYMENT COMPLETE!"