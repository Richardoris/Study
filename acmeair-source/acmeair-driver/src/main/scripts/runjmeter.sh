#!/bin/bash
DATE=$(date +%Y-%m-%d-%s)
nohup /home/lichen/apache-jmeter/bin/jmeter -n -t AcmeAir.jmx -j logs/AcmeAir_$DATE.log -l AcmeAir1.jtl >logs/AcmeAir_$DATE.stdout 2>logs/AcmeAir_$DATE.stderr &


