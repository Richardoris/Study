#/bin/sh

if [ $# -ne 3 ]
    then
        echo "you need three arguments,auto_perfomance_ui.sh dir/file dir/file (0 or 1),0 is relative time ,1 is in absolute time"
        exit 1
fi
a=`lsb_release -d|awk '{print $2}'`

if [ "$a" = "Ubuntu" ];then
    sudo apt-get install python-numpy python-matplotlib
else
    sudo yum install python-numpy python-matplotlib
fi

if [ ! -d "pyNmonAnalyzer/src" ]; then 
 git clone https://github.com/madmaze/pyNmonAnalyzer.git
fi 

cd pyNmonAnalyzer/src/
python pyNmonAnalyzer.py --defaultConfig
python pyNmonAnalyzer.py -x -b -o ../../Report -i $1
python pyNmonAnalyzer.py -x -b -o ../../Report_agent -i $2

cd ../../


if [ $3 -eq 0 ]; then
    name1=`basename $1 |awk '{print substr($1,0,index($1,".")-1)}'`
    name2=`basename $2 |awk '{print substr($1,0,index($1,".")-1)}'`
    
    cat Report/csv/CPU_ALL.csv |awk -F \, 'NR==2{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))}NR>=2{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))-a"|"100-$5}'> test_1_cpu_tmp

    cat Report/csv/MEM.csv |awk -F \, 'NR==2{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))}NR>=2{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))-a"|"$12}'> test_1_mem_tmp

    cat Report/csv/NET.csv |awk -F \, 'NR==2{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))}NR>=2{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))-a"|"$2"|"$5}'> test_1_net_tmp

    awk -F \| 'FILENAME=="test_1_cpu_tmp"{cpu[$1]=$2};FILENAME=="test_1_mem_tmp"{if($1 in cpu){print $1"|"cpu[$1]"|"$2}}' test_1_cpu_tmp test_1_mem_tmp > nmon_target_tmp

    awk -F \| 'FILENAME=="test_1_net_tmp"{net[$1]=$2"|"$3};FILENAME=="nmon_target_tmp"{if($1 in net){print $1"|"$2"|"$3"|"net[$1]}}' test_1_net_tmp  nmon_target_tmp > $name1




    cat Report_agent/csv/CPU_ALL.csv |awk -F \, 'NR==2{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))}NR>=2{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))-a"|"100-$5}'> test_2_cpu_tmp

    cat Report_agent/csv/MEM.csv |awk -F \, 'NR==2{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))}NR>=2{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))-a"|"$12}'> test_2_mem_tmp

    cat Report_agent/csv/NET.csv |awk -F \, 'NR==2{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))}NR>=2{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($1,12,2)" "substr($1,15,2)" "substr($1,18,2))-a"|"$2"|"$5}'> test_2_net_tmp

    awk -F \| 'FILENAME=="test_2_cpu_tmp"{cpu[$1]=$2};FILENAME=="test_2_mem_tmp"{if($1 in cpu){print $1"|"cpu[$1]"|"$2}}' test_2_cpu_tmp test_2_mem_tmp > nmon_target_agent_tmp

    awk -F \| 'FILENAME=="test_2_net_tmp"{net[$1]=$2"|"$3};FILENAME=="nmon_target_agent_tmp"{if($1 in net){print $1"|"$2"|"$3"|"net[$1]}}' test_2_net_tmp  nmon_target_agent_tmp > $name2


    python acm_performance_nmon_UI.py $name1 $name2

    rm test_1_cpu_tmp test_1_mem_tmp test_1_net_tmp nmon_target_tmp test_2_cpu_tmp test_2_mem_tmp test_2_net_tmp nmon_target_agent_tmp $name1 $name2


elif [ $3 -eq 1 ]; then
    name1=`basename $1 |awk '{print substr($1,0,index($1,".")-1)}'`
    name2=`basename $2 |awk '{print substr($1,0,index($1,".")-1)}'`
    cat Report/csv/CPU_ALL.csv |awk -F \, '{print substr($1,12,8)"|"100-$5}'> test_1_cpu_tmp
    cat Report/csv/MEM.csv |awk -F \, '{print substr($1,12,8)"|"$12}'> test_1_mem_tmp
    cat Report/csv/NET.csv |awk -F \, '{print substr($1,12,8)"|"$2"|"$5}'> test_1_net_tmp
    awk -F \| 'FILENAME=="test_1_cpu_tmp"{cpu[$1]=$2};FILENAME=="test_1_mem_tmp"{if($1 in cpu){print $1"|"cpu[$1]"|"$2}}' test_1_cpu_tmp test_1_mem_tmp > nmon_target_tmp
    awk -F \| 'FILENAME=="test_1_net_tmp"{net[$1]=$2"|"$3};FILENAME=="nmon_target_tmp"{if($1 in net){print $1"|"$2"|"$3"|"net[$1]}}' test_1_net_tmp  nmon_target_tmp > $name1
   
    cat Report_agent/csv/CPU_ALL.csv |awk -F \, '{print substr($1,12,8)"|"100-$5}'> test_2_cpu_tmp
    cat Report_agent/csv/MEM.csv |awk -F \, '{print substr($1,12,8)"|"$12}'> test_2_mem_tmp
    cat Report_agent/csv/NET.csv |awk -F \, '{print substr($1,12,8)"|"$2"|"$5}'> test_2_net_tmp
    awk -F \| 'FILENAME=="test_2_cpu_tmp"{cpu[$1]=$2};FILENAME=="test_2_mem_tmp"{if($1 in cpu){print $1"|"cpu[$1]"|"$2}}' test_2_cpu_tmp test_2_mem_tmp > nmon_target_agent_tmp
    
    awk -F \| 'FILENAME=="test_2_net_tmp"{net[$1]=$2"|"$3};FILENAME=="nmon_target_agent_tmp"{if($1 in net){print $1"|"$2"|"$3"|"net[$1]}}' test_2_net_tmp  nmon_target_agent_tmp > $name2
    
    python acm_performance_nmon_UI.py $name1 $name2

    rm test_1_cpu_tmp test_1_mem_tmp test_1_net_tmp nmon_target_tmp test_2_cpu_tmp test_2_mem_tmp test_2_net_tmp nmon_target_agent_tmp $name1 $name2
fi

