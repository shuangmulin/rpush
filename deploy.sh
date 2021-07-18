#!/bin/bash
declare -A jarMap=(
  ["eureka"]="rpush-eureka-0.0.1-SNAPSHOT.jar"
  ["zuul"]="rpush-zuul-0.0.1-SNAPSHOT.jar"
#  ["scheduler"]="rpush-scheduler-0.0.1-SNAPSHOT.jar"
  ["route1"]="rpush-route-0.0.1-SNAPSHOT.jar"
  ["server1"]="rpush-server-0.0.1-SNAPSHOT.jar"
)

java="/opt/appcenter/jdk/jdk1.8/bin/java"
basePath="/opt/appcenter/jar/rpush" # 根路径

function usage() {
  echo -e "\e[36m命令使用示例: deploy.sh var1 var2"
  echo -e "var1: assign the operation} signal:
    start                 --启动服务（如果服务已启动，这个命令会先停止该服务再启动）
    stop                  --停止服务
    status                --获取服务状态
    init                  --初始化目录结构"
  echo -e "var2: assign the application to operate:"
  echo "    all ==> 所有服务"
  for key in "${!jarMap[@]}"; do
    echo "    ${key} ==> ${key}服务"
  done

}

function progress() {
  b="#"
  sleeptime=$(echo "$1 50" | awk '{printf("%0.2f", $1/$2)}')
  for ((i = 1; $i <= 50; i += 1)); do
    prog=$(echo "$i" | awk '{printf("%0.0f", 2*$1)}')
    progline="progress:[%-50s]%d%%\r"
    printf "${progline}" ${b} ${prog}
    b=#${b}
    sleep ${sleeptime}
  done
  echo
}

function init() {
  local appName=$1    # 服务名称
  local jarPath=$2    # jar包路径 = 根路径 + 服务名称
  local configPath=$3 # 配置文件路径 = jar包路径 + yml
  local logPath=$4    # 日志路径
  echoCommon "########################################[init ${appName}]########################################"
  if [ ! -x "${jarPath}" ]; then
    echoCommon "jar包路径不存在，尝试新建..."
    mkdir -p "${jarPath}"
  fi
  if [ ! -x "${configPath}" ]; then
    echoCommon "配置文件路径不存在，尝试新建..."
    mkdir -p "${configPath}"
  fi
  if [ ! -x "${logPath}" ]; then
    echoCommon "日志文件路径不存在，尝试新建..."
    mkdir -p "${logPath}"
  fi

  if [ -x "${jarPath}" ]; then
    echoSuccess "[${appName}]目录结构初始化成功..."
    echoSuccess "   jar包路径：${jarPath}"
    echoSuccess "配置文件路径：${configPath}"
    echoSuccess "日志文件路径：${logPath}"
  else
    echoError "[${appName}]目录结构初始化失败..."
  fi
}

function echoError() {
  echo -e "\033[31m $1 \033[0m"
}

function echoCommon() {
  echo -e "\033[36m $1 \036"
}

function echoSuccess() {
  echo -e "\033[32m $1 \033[0m"
}

function getPID() {
  ps -ef | grep "$1/$2" | egrep -v "grep|$$" | awk 'NR==1{print $2}'
}

function start() {
  local appName=$1    # 服务名称
  local jarPath=$2    # jar包路径=根路径+服务名称
  local configPath=$3 # 配置文件路径=jar包路径+yml
  local logPath=$4    # 日志路径
  local jarName=$5    # jar包名称

  echoCommon "########################################[启动 ${appName}]########################################"
  local PID
  PID=$(getPID "$configPath")
  # 先停服务
  [ -n "$PID" ] && kill -9 $PID

  if [ ! -f "${jarPath}/${jarName}" ]; then
    echoError "错误：jar包[ ${jarPath}/${jarName} ]不存在，请确认！"
    exit
  fi

  if [ ! -f "${jarPath}/${jarName}" ]; then
    echoError "错误：配置文件[ ${configPath}/application.yml ]不存在，请确认！"
    exit
  fi

  if [ ! -x "${logPath}" ]; then
    echoError "错误：日志文件夹[ ${logPath} ]不存在，请确认！"
    exit
  fi

  cd "${jarPath}" || exit
  local s
  s="nohup ${java} -Xms256m -Xmx256m -Dfile.encoding=UTF-8 -Duser.timezone=GMT+08 -jar ${jarPath}/${jarName} --spring.config.location=${configPath}/application.yml > ${logPath}/${appName}.log 2>&1 &"
  echo "${s}"
  nohup ${java} -Xms256m -Xmx256m -Dfile.encoding=UTF-8 -Duser.timezone=GMT+08 -jar ${jarPath}/${jarName} --spring.config.location=${configPath}/application.yml >${logPath}/${appName}.log 2>&1 &
  progress 3

  PID=$(ps -ef | grep "${jarPath}/${jarName}" | egrep -v "grep|$$" | awk 'NR==1{print $2}')
  echoSuccess "[${appName}]启动成功..."
  echoSuccess "日志文件路径：${logPath}"
}

function stop() {
  local PID
  PID=$(getPID "$configPath")
  echoCommon "########################################[停止 $3]########################################"
  [ -z "$PID" ] && echoCommon "PID: $PID"
  # 停服务
  [ -n "$PID" ] && kill -9 $PID
  PID=$(getPID "$configPath")
  if [ "$PID" == "" ]; then
    echoSuccess "[${appName}]已停止..."
  fi
}

function status() {
  local PID
  PID=$(getPID "$configPath")
  echoCommon "########################################[$3 状态]########################################"
  if [ "$PID" == "" ]; then
    echoCommon "[${appName}]已停止..."
  else
    echoCommon "[${appName}]正常运行，进程ID：$PID..."
  fi
}

function main() {
  if [[ $# -lt 2 || $# -gt 3 ]]; then
    usage
    exit
  fi

  local appNameParam=$2 # 服务名称参数
  local operation=$1

  for key in ${!jarMap[*]}; do
    local jarName=${jarMap[$key]} # jar包名称
    local appName=${key}          # 服务名称

    if [ "${appNameParam}" != "all" ] && [ "${appNameParam}" != "${appName}" ]; then
      continue
    fi

    local jarPath=${basePath}                  # jar包路径 = 根路径
    local configPath=${jarPath}/yml/${appName} # 配置文件路径 = jar包路径 + yml + 服务名称
    local logPath=${jarPath}/logs/             # 日志路径 + logs + 服务名称
    case "${operation}" in
    "init")
      # 初始化目录结构
      init "${appName}" "${jarPath}" "${configPath}" "${logPath}"
      ;;
    "start")
      # 服务启动
      start "${appName}" "${jarPath}" "${configPath}" "${logPath}" "${jarName}"
      ;;
    "stop")
      # 服务停止
      stop "${jarPath}" "${jarName}" "${appName}"
      ;;
    "status")
      # 服务状态
      status "${jarPath}" "${jarName}" "${appName}"
      ;;
    *)
      usage
      exit
      ;;
    esac
  done
}

main "$@"
