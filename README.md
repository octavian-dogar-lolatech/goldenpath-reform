# GoldenPath
GoldenPath wants to be an universal startup project for any kind of Java Rest API using SpringBoot and include features like Logging, Monitoring, Security, Authentification, etc.

## How to run

1.```git clone project``` 

2.```cd project_path```

3.```docker-compose up -d```

4.```mvn clean install```

5.```mvn spring-boot:run```

6.```docker run --name=prometheus -p 9090:9090 -v /[project_location]/GoldenPath-master/etc/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml```

7.```docker run -d --name=grafana -p 3000:3000 grafana/grafana```

8.Access http://localhost:3000 to launch Grafana, the metrics visualization tool. Temporary credentials are admin:admin



### TODO List

~~a. Add custom implementation for metrics, health information~~

~~b. Add Swagger Documentation~~

~~c. Add database connection (H2 is used in this moment)~~


 
