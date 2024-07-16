# Open-Job

<p align="center">
🔥2022 最新的轻量级分布式任务调度系统
</p>

<p align="center">
  <a href="https://search.maven.org/search?q=g:com.openbytecode%20a:open-starter-*">
    <img alt="maven" src="https://img.shields.io/github/v/release/lijunping365/Open-Job?include_prereleases&logo=Open-Job&style=plastic">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="licenses" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>

  <a href="https://github.com/lijunping365/Open-Job">
    <img alt="github" src="https://badgen.net/github/stars/lijunping365/Open-Job?icon=github" >
  </a>
  
  <a href="https://github.com/lijunping365/Open-Job">
      <img alt="forks" src="https://badgen.net/github/forks/lijunping365/Open-Job?icon=github&color=4ab8a1" >
    </a>
</p>

## 🎨 Open-Job 介绍

[项目官网地址](http://www.openbytecode.com/)

[项目官方文档地址](http://www.openbytecode.com/project/open-job/docs/quick-start)

## ✨ 已实现功能点

1. 定时任务基于时间轮算法实现，支持动态修改任务状态，同时支持拓展其他实现方式

2. 客户端与服务端通信采用 Grpc，同时支持 Netty

3. 注册中心支持 Nacos、Zookeeper，同时支持拓展其他注册中心，而且支持节点动态上线下线

4. 执行器支持集群部署，支持负载均衡，默认提供了一致性hash、随机权重算法，支持多种容错机制，默认提供了失败重试、故障转移等机制，负载均衡和容错都支持拓展

5. 任务监控报警能力支持

6. 前后端分离，管理后台基于 antd-pro 搭建

7. 支持多应用任务调度

8. 支持调度脚本

9. 支持前后端统一部署，代码分支为：beta

10. 调度完成后可查看任务具体是哪个节点执行的，可在任务执行日志中查看

11. 支持任务分片执行，解决大任务问题，大大提升任务执行效率

## 🍪 快速开始

### 1 搭建任务管理系统

1. 下载本项目

```
git clone https://github.com/lijunping365/Open-Job.git
```

2. 创建数据库表

sql 文件在 doc/db/open_job.sql

3. 启动 Dashboard 服务

```java
@EnableSecurity
@EnableScheduling
@EnableOpenRpcClient
@SpringBootApplication
public class JobDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobDashboardApplication.class, args);
    }
}
```

4. 启动执行器

```java
@EnableOpenRpcServer
@SpringBootApplication
public class JobClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobClientApplication.class, args);
    }
}
```

5. 下载前端项目

```
git clone https://github.com/lijunping365/Open-Job-Admin.git
```

6. 安装依赖

```
npm install
```

7. 启动前端项目

```
npm start
```

### 2 创建 JobHandler

示例1

```java
@Slf4j
@JobHandler(name = "job-one")
@Component
public class OpenJobHandlerOne implements OpenJobHandler {

    @Override
    public void handler(JobParam jobParam) {
        log.info("JobHandlerOne 处理任务");
    }
}
```

示例2

```java
@Slf4j
@Component
public class OpenJobHandlerMethodOne{

    @JobHandler(name = "job-method-one1")
    public void handlerOne1(JobParam jobParam) {
        log.info("JobHandlerOne 处理任务, 任务参数 {}", jobParam.getParams());
    }

    @JobHandler(name = "job-method-one2")
    public void handlerOne2(JobParam jobParam) {
        log.info("JobHandlerOne 处理任务, 任务参数 {}", jobParam.getParams());
    }
}
```

## ❓ FAQ

有问题可以提 issues，我会及时解答

## Contributing

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/lijunping365/Open-Job/issues/) 讨论新特性或者变更。

## Copyright and License

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。如有需要可邮件联系作者免费获取项目授权。

- Licensed under the Apache License v2.0.
- Copyright (c) 2022-present, lijunping.

## 🎉收尾

1. 欢迎大家的关注和使用，欢迎 star，本项目将持续更新

2. 欢迎接入的公司在 [登记地址](https://github.com/lijunping365/Open-Job/issues/1 ) 登记，登记仅仅为了产品推广。


## 更新记录

v2.0.0

为了轻量，注册中心去掉了 zookeeper 和 nacos 的实现，改为 db 实现。

执行器自动摘除： 调度中心启动之后会向执行器发送心跳健康检测，默认 30s 一次，如果连续 3次 心跳检测失败则自动摘除执行器