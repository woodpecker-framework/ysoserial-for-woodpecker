# ysoserial-for-woodpecker

## 0x01 简介
`ysoserial-for-woodpecker`是基于 [ysoserial](https://github.com/frohoff/ysoserial) ,为woodpecker框架定制开发的gadget集成库。

## 0x02 功能
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
- [ ] bcel 注入bcel字符串，实现代码执行
- [ ] bcel_class_file 通过文件注入
- [ ] script_file 通过js引擎执行代码
- [ ] script_base64  通过js引擎执行代码
- [ ] loadjar 调用jar中类的无参构造器
- [ ] loadjar_with_args 调用jar中类的参数为一个String的构造器
- [ ] jndi jndi注入
