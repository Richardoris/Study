################################################################################
# Copyright (c) 2014 IBM Corp.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
################################################################################
FROM acmeair/tomcat

RUN mkdir /opt/tomcat/webapps/ROOT

ADD acmeair-webapp-tc7.war /opt/tomcat/webapps/ROOT/

RUN  cd /opt/tomcat/webapps/ROOT &&\
  jar xf acmeair-webapp-tc7.war &&\
  rm acmeair-webapp-tc7.war

ADD ACMEAIR_WEBAPP-docker.properties /opt/tomcat/webapps/ROOT/WEB-INF/classes/ACMEAIR_WEBAPP-docker.properties
ADD config.properties /opt/tomcat/webapps/ROOT/WEB-INF/classes/config.properties
ADD log4j.properties /opt/tomcat/webapps/ROOT/WEB-INF/classes/log4j.properties

ENV CATALINA_OPTS -Xmx512m
ENV SERVICE_NAME webapp

CMD ["/usr/bin/supervisord", "-n"]
