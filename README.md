# iTennis批量化运维平台4.0

旨在简化用户对服务器的管理，提供Linux服务器相关的运维功能。

## 1、功能列表

| 功能选项 | 功能名称                        | 说明                                                         |
| -------- | ------------------------------- | ------------------------------------------------------------ |
| [1]      | 配置PXE环境                     | 自动化安装（Redhat和Centos）操作系统，目前测试Linux版本6和版本7均支持，支持EFI和UEFI两种安装模式。 |
| [2]      | 批量双网卡绑定Bond模式配置      | 支持做一组或者多组Bond模式的双网卡绑定                       |
| [3]      | 时区和时间配置                  | 支持同步配置所有服务器时间和时区                             |
| [4]      | 批量配置RAID                    | 支持批量配置RAID0和RAID1和RAID5等                            |
| [5]      | 收集硬件配置信息,检查网络连通性 | 可以测试网络连通性，收集cpucore、内存、磁盘数、和磁盘大小的信息 |
| [6]      | 批量配置YUM源                   | 支持批量配置所有服务器的Yum仓库，和本地Yum配置。             |
| [7]      | 批量拷贝文件                    | 将本地文件或者文件夹拷贝所有服务器                           |
| [8]      | 批量执行命令                    | 批量在所有服务器上执行命令                                   |
| [9]      | 批量拷贝文件到本地              | 将所有服务器上的某个文件或者文件夹拷贝到本地服务器上，可以作为收集信息功能。 |
| [10]     | 解决重装操作系统无法引导        | 解决二次安装操作系统无法引导问题，清除MBR引导。              |
| [11]     | 服务器免密登录                  | 本机免密钥访问其他机器和服务器之间免密登录                   |
| [12]     | 批量更换IP地址                  | 可以更换旧IP地址为新IP地址，支持多网卡修改                   |
| [13]     | 批量配置BMC地址                 | 可以批量配置所有服务器的BMC地址                              |
| [14]     | 批量修改文件内容                | 修改、替换和删除内容（包括配置文件中的值或任意文本）         |
| [15]     | 批量更换用户登录密码            | 可以批量更换ROOT密码，或者普通用户的密码                     |
| [16]     | 检查文件权限及属组              | 检查某个文件夹下的所有文件权限和属组是否满足                 |
| [m]      | 更多                            | 更多                                                         |

以上功能，可以两两搭配，可做的效果更多，比如批量上传+批量执行，可以做任何批量部署功能。

**修复的Bug情况：**

​	1、第二版中修改RAID卡驱动导致安装操作系统无法识别硬盘的问题



## 2、运行

- 项目下载到本地；
- 可以修改配置文件resources目录下的applicaiton.yml文件，修改端口号和pxe相关的配置。
- 运行脚本start.sh，脚本会将本地远端的项目拉下来编译成jar包，然后将需要的配置文件和核心功能文件拷贝到当前目录jobs文件夹下，并在服务器后端运行任务。
- 相关功能的配置文件都在conf文件夹下配置。
- 运行http://127.0.0.1:8081/swagger-ui.html，可以查看相关接口信息



## 3、配置文件介绍

1. 主要配置文件hosts.conf，基本功能都是基于这个配置文件，提供注解功能。

   ```linux
   #双网卡的情况下，可以写管理地址也可以写业务地址，也可以都写，以空格或者制表符分割 ,注解符号#或者//
   #[临时IP地址  | 临时IP地址]  用户名  密码
   192.168.0.102  root 1234
   127.0.0.1  apple  1234
   192.168.0.103  root 123456
   ```

2. 功能配置文件（比如修改ip地址的配置文件change_ip.conf，配置raid的配置文件raid.conf等）

   ```
   每个文件中都有写法的描述。可以参考写法实现相应的功能
   ```

   

## 4、使用的技术栈

- **SpringBoot2**
- **SpringCloud Config**
- **Linux shell**
- **Swagger2**



## 5、联系人

Hotmail：xiehuisheng@hotmail.com

微信：xhs563376097