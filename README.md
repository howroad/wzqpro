
已完成:

注册登陆页面

聊天页面包括 在线玩家列表/消息显示/消息发送/查看玩家游戏段位/发起对战
五子棋页面包括 准备/开始/退出/消息发送和显示

待完成

玩家下线的判断/玩家结束游戏的判断/登陆单机版本/一局游戏结束后重新准备开始/电脑下棋复合棋型分数设置不合理(包括双活二和单活三,三活二等)

第三次改动:
1.添加了对战功能,增加了对战判断(是否选择,选择的是否为自己,对方是否正在战斗)
2.发起对战后按钮变化
3.棋盘简化
4.几个文件位置的调整
5.其他的改动

20180203改动:
1.数据库微调
create table tb_user(id int auto_increment primary key,username varchar(20),password varchar(20),nickname varchar(20),registtime datetime);
create table tb_wzq(id int auto_increment primary key,user_id int,win int,lost int,ping int,//平局taopao int,score int);
2.实现了准备/逃跑/短线/下线等功能
3.实现了对战中聊天功能
4.实现了服务器下线的判断
5.玩家状态改变更新列表
6.输赢逃跑时数据库的写入
7.改了一些BUG

待完成:
1.单机版算法更新
2.求和/认输/悔棋功能
3.私聊和群聊的切换
