### 手动场景和自动化场景

技术：
1. 定时任务，延时用quartz实现
2. 场景下的任务用messageQueue实现 
3. action中除去【延时】等附加因素会又顺序关系，其他均之间没有关联，只负责执行
4. trigger入口待定
5. 把场景做成服务，开放出去


### util

1. api 第三方相关
2. basic 基础工具类（时间，正则等等）
3. tool 复杂工具类（邮件，excel等）


**19.11.20**````
- 完善excel/mail工具类及调用demo

**19.11.21** 
- 增加数组/List转换
- 增加网络请求工具类
- 补充基础工具类

**19.11.22**
- 增加String相关的拼接demo
- 增加AES加密工具类
- 增加浮点数工具类

**19.11.23**
- 增加文件下载，整合excel
- 增加字节工具类

**19.11.27**
- 增加spring-mongo工具类

**19.1.03**
- mongo工具类增加$bucket等demo
- mongo下常规查询和性能查询
