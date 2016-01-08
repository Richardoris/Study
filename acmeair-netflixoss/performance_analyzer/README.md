Jmeter Nmon log analyze(UI)
======================
how to use the UI tools
## The method of Jmeter log

```bash
git clone gitlab@git.oneapm.me:research/acmeair-netflixoss.git
cd acmeair-netflixoss/performance_analyzer/jmeter_analyzer 目录下
./dependencies.sh
./auto_performance_ui.sh 绝对路径/AcmeAir_2015-11-25-1448418709.log 绝对路径/AcmeAir_2015-11-24-1448365002.log 1
```
auto_performance_ui.sh 有三个参数,前两个参数需要绝对路径加文件名,第三个参数0代表的是图形化界面横坐标为相对时间,1代表的是图形化界面横坐标为绝对时间.

## The method of Nmon log

```bash
git clone gitlab@git.oneapm.me:research/acmeair-netflixoss.git
cd acmeair-netflixoss/performance_analyzer/nmon_analyzer 目录下
 ./dependencies.sh
./auto_perfomance_ui.sh 绝对路径/zuul1.nmon.agent 绝对路径/zuul1.nmon 1
```
auto_performance_ui.sh 有三个参数,前两个参数需要绝对路径加文件名,第三个参数0代表的是图形化界面横坐标为相对时间,1代表的是图形化界面横坐标为绝对时间.
