#/bin/sh

if [ $# -ne 3 ]
    then
        echo "you need three arguments,auto_perfomance_ui.sh dir/file dir/file (0 or 1),0 is relative time ,1 is in absolute time"
        exit 1
fi


if [ $3 -eq 0 ]; then
    name1=`basename $1 |awk '{print substr($1,0,index($1,".")-1)}'`
    name2=`basename $2 |awk '{print substr($1,0,index($1,".")-1)}'`

    cat $1 |grep "summary +"|awk 'NR==1{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($2,0,2)" "substr($2,4,2)" "substr($2,7,2))}NR>=1{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($2,0,2)" "substr($2,4,2)" "substr($2,7,2))-a"|"$8"|"substr($12,0,length($12)-2)"|"$14"|"$16"|"$18}' > $name1
    cat $2 |grep "summary +"|awk 'NR==1{a=mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($2,0,2)" "substr($2,4,2)" "substr($2,7,2))}NR>=1{print mktime(substr($1,0,4)" "substr($1,6,2)" "substr($1,9,2)" "substr($2,0,2)" "substr($2,4,2)" "substr($2,7,2))-a"|"$8"|"substr($12,0,length($12)-2)"|"$14"|"$16"|"$18}' > $name2

    python acm_performance_UI.py $name1 $name2

    rm $name1 $name2

elif [ $3 -eq 1 ]; then
    name1=`basename $1 |awk '{print substr($1,0,index($1,".")-1)}'`
    name2=`basename $2 |awk '{print substr($1,0,index($1,".")-1)}'`

    cat $1 |grep "summary +"|awk '{print $2"|"$8"|"substr($12,0,length($12)-2)"|"$14"|"$16"|"$18}' > $name1
    cat $2 |grep "summary +"|awk '{print $2"|"$8"|"substr($12,0,length($12)-2)"|"$14"|"$16"|"$18}' > $name2

    python acm_performance_UI.py $name1 $name2

    rm $name1 $name2
fi
