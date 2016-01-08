import sys
import time
import datetime
import json
import tornado.template
class gen_report:

    def __init__(self):
        self.conf = {}
        self.data = {}

    def get_oridata(self,file1,file2):
        list_cpu=[]
        list_mem=[]
        list_eth0_read=[]
        list_eth0_write=[]
        list_coordinate=[]
        result_data={}
        performance_data={}
        f1=open(file1,"r")
        for i in f1.readlines():
            a=i.strip().split("|")
            #performance_data["cpu"]=float(a[1])
            #performance_data["mem"]=float(a[2])
            #performance_data["eth0_read"]=float(a[3])
            #performance_data["eth0_write"]=float(a[4])
            #result_data[float(a[0])]=performance_data
            list_coordinate.append(a[0])
            list_cpu.append(float(a[1]))
            list_mem.append(float(a[2]))
            list_eth0_read.append(float(a[3]))
            list_eth0_write.append(float(a[4]))
        #self.data['ori'] = result_data
        self.data['ori_cpu'] = list_cpu
        self.data['ori_mem'] = list_mem
        self.data['ori_eth0_read'] = list_eth0_read
        self.data['ori_eth0_write'] = list_eth0_write
        self.data['x']=list_coordinate
        self.data['name1']=file1
        if len(list_coordinate)/10 ==0:
            self.data['interval']=1
        else:
            self.data['interval']=len(list_coordinate)/10
        list_cpu_later=[]
        list_mem_later=[]
        list_eth0_read_later=[]
        list_eth0_write_later=[]
        performance_data_later={}
        result_data_later={}
        f2=open(file2,"r")
        for i in f2.readlines():
            a=i.strip().split("|")
            #performance_data_later["cpu"]=float(a[1])
            #performance_data_later["mem"]=float(a[2])
            #performance_data_later["eth0_read"]=float(a[3])
            #performance_data_later["eth0_write"]=float(a[4])
            #result_data_later[float(a[0])]=performance_data_later
            list_cpu_later.append(float(a[1]))
            list_mem_later.append(float(a[2]))
            list_eth0_read_later.append(float(a[3]))
            list_eth0_write_later.append(float(a[4]))
        #self.data['ori_later'] = result_data_later
        self.data['ori_cpu_later'] = list_cpu_later
        self.data['ori_mem_later'] = list_mem_later
        self.data['ori_eth0_read_later'] = list_eth0_read_later
        self.data['ori_eth0_write_later'] = list_eth0_write_later
        self.data['name2']=file2
        return self.data

    def generate(self,file1,file2):
        loader = tornado.template.Loader("./")
        t = loader.load("performance_use_nmon.html")
        self.conf = self.get_oridata(file1,file2)
        html = t.generate(static_url="../static",conf=self.conf)
        today = datetime.datetime.fromtimestamp(time.time()).strftime("%Y-%m-%d %H:%M:%S")
        f = open("./"+file1+"_"+file2+" %s.html"%(today),"w")
        f.write(html)

if __name__ == '__main__':
    d = gen_report().get_oridata(sys.argv[1],sys.argv[2])
    gen_report().generate(sys.argv[1],sys.argv[2])
    print d
