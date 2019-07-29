git clone https://github.com/deviantony/docker-elk.git
cp docker-compose.yml docker-elk/docker-compose.yml
cp logstash.conf docker-elk/logstash/pipeline/logstash.conf
cd docker-elk
docker-compose up -d