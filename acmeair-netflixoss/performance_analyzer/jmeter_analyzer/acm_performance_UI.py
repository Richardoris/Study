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
        list_tps=[]
        list_rt=[]
        list_coordinate=[]
       # result_data={}
       # performance_data={}
        f1=open(file1,"r")
        for i in f1.readlines():
            a=i.strip().split("|")
           # performance_data["tps"]=float(a[2])
           # performance_data["rt"]=float(a[3])
           # result_data[float(a[0])]=performance_data
            list_coordinate.append(a[0])
            list_tps.append(float(a[2]))
            list_rt.append(float(a[3])) 
       # self.data['ori'] = result_data
        self.data['ori_tps'] = list_tps
        self.data['ori_rt'] = list_rt
        self.data['x']=list_coordinate
        self.data['name1']=file1
        list_tps_later=[]
        list_rt_later=[]
        #performance_data_later={}
        result_data_later={}
        f2=open(file2,"r")
        for i in f2.readlines():
            a=i.strip().split("|")
           # performance_data_later["tps"]=float(a[2])
           # performance_data_later["rt"]=float(a[3])
           # result_data_later[float(a[0])]=performance_data_later
            list_tps_later.append(float(a[2]))
            list_rt_later.append(float(a[3])) 
        #self.data['ori_later'] = result_data_later
        self.data['ori_tps_later'] = list_tps_later
        self.data['ori_rt_later'] = list_rt_later
        self.data['name2']=file2

        return self.data

    def generate(self,file1,file2):
        loader = tornado.template.Loader("./")
        t = loader.load("performance_use.html")
        self.conf = self.get_oridata(file1,file2)
        html = t.generate(static_url="../static",conf=self.conf)
        today = datetime.datetime.fromtimestamp(time.time()).strftime("%Y-%m-%d %H:%M:%S")
        f = open("./"+file1+"_"+file2+" %s.html"%(today),"w")
        f.write(html)

if __name__ == '__main__':
    d = gen_report().get_oridata(sys.argv[1],sys.argv[2])
    gen_report().generate(sys.argv[1],sys.argv[2])
    print d
