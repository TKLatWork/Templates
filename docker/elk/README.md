## ELK

    一个Docker和ELK整合的示例，ELK和Filebeat都是在docker。
    通过监视docker的日志目录和sock，把附加了docker meta信息的日志集中到ELK。

## 使用

	检查基本的配置文件，确认有没需要修改的。 比如filebeat.yml
	运行各自的startup.sh

### ELK

	elk/curator 应该是个清理数据的脚本

### filebeat

	使用6版本的特性。

### logstash

	有必要的话调整filter

## TODO

	filebeat把自己甚至ELK的日志也提交了，略重复。

# LOG

	搬过来了，未整理