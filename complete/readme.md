### 版本约定
- SpringBoot 2.7.1
- spring native 0.12.1 
- paketobuildpacks/builder:0.1.253-tiny
- paketobuildpacks/run:1.3.72-tiny-cnb

### 离线安装步骤
1. 避免每次到docker.io查找镜像,可使用本地registry
    - 启动registry `docker run -d -p 5000:5000 -v /opt/data/registry:/tmp/registry registry`
    - 标记builder `docker tag paketobuildpacks/builder:0.1.253-tiny localhost:5000/paketobuildpacks/builder:0.1.253-tiny`
    - 推送builder `docker push localhost:5000/paketobuildpacks/builder:0.1.253-tiny`
    - 标记run `docker tag paketobuildpacks/run:1.3.72-tiny-cnb localhost:5000/paketobuildpacks/run:1.3.72-tiny-cnb`
    - 推送run `docker push localhost:5000/paketobuildpacks/run:1.3.72-tiny-cnb`
   
2. 本地下载`https://download.bell-sw.com/vm/22.2.0/bellsoft-liberica-vm-core-openjdk11.0.16.1+1-22.2.0+3-linux-amd64.tar.gz`和`https://github.com/anchore/syft/releases/download/v0.55.0/syft_0.55.0_linux_amd64.tar.gz`
   - 本地搭建nginx,浏览器能正常下载,但构建时报错`dial tcp 127.0.0.1:443: connect: connection refused`,可能要配置信任证书,还需要进一步验证

### 容器测试
0. 运行容器 `docker run -d -p 8080:8080 rest-service-complete:lastest`
1. 普通HTTP请求 `curl http://localhost:8080/greeting?name=ypq` , 状态码200
2. H2数据库测试 `curl http://localhost:8080/increaseAndGetAge?userId=1` , 状态码200