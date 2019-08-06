# 创建远程用户
CREATE USER `noah`@`%` IDENTIFIED BY 'Noah1115'
SELECT * FROM USER;

DROP DATABASE IF EXISTS comeondb;
CREATE DATABASE comeondb;
USE comeondb;

# 修改数据库的字符集
ALTER DATABASE comeondb CHARACTER SET utf8;

# 建表
# 用户登陆表
DROP TABLE IF EXISTS user_login;
CREATE TABLE user_login(
	`id` INT(11) NOT NULL COMMENT '用户登录表编号' PRIMARY KEY AUTO_INCREMENT, #非空 主键，标识列
	`userPhone` VARCHAR(20) NOT NULL UNIQUE KEY COMMENT '用户手机号码', #唯一
	`userPassword` VARCHAR(20) NOT NULL COMMENT '用户密码', 
	`lastModifiedTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次修改时间' #timestamp类型，最近一次修改时间，默认为当前时间
)CHARSET='UTF8' ENGINE=INNODB COMMENT="用户登录表";


# 用户登录记录表
DROP TABLE IF EXISTS user_login_log;
CREATE TABLE user_login_log(
	`userLoginId` INT(11) NOT NULL COMMENT '用户登陆表编号', #非空  引自用户登录表
	`loginTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登陆时间', #timestamp类型，最近一次修改时间，默认为当前时间
	`loginStatus` TINYINT NOT NULL DEFAULT 0 COMMENT '登录状态', #tinyint数据类型，0：登录成功；1：登录失败。默认为0
	`loginIP`  INT(10) COMMENT '用户登录的IP地址'  #可空，记录用户登录的IP地址
)CHARSET='UTF8' ENGINE=INNODB COMMENT='用户登录记录表';


# 用户信息表
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info(
	`userId` INT(11) NOT NULL COMMENT '用户编号' PRIMARY KEY AUTO_INCREMENT, #非空 主键，标识列
	`userPhone` VARCHAR(20) NOT NULL UNIQUE KEY COMMENT '用户手机号码', #唯一
	`userNickName` VARCHAR(10) NOT NULL DEFAULT '小明' COMMENT '用户昵称', #非空 默认为：小明
	`userBirthday` DATETIME COMMENT '用户生日', #可空
	`userGender` CHAR(2) NOT NULL DEFAULT '男' COMMENT '用户性别', 
	`headIcon` LONGBLOB COMMENT '用户头像', #用户头像 longblob类型
	`description` VARCHAR(100) DEFAULT '这个人很无聊欸，什么都不说！' COMMENT '用户的自我叙述', #默认：这个人很无聊欸，什么都不说！
	`registerTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '用户注册时间', #默认为当前时间
	`acceptedDistance` DOUBLE COMMENT '用户可以接受的组团距离', #单位为km
	`identityCardNo` VARCHAR(18) UNIQUE KEY COMMENT '身份证号', #唯一
	`userStatus` TINYINT NOT NULL DEFAULT 0 COMMENT '用户状态' #tinyint数据类型，0：活跃；1：冻结
)CHARSET='UTF8' ENGINE=INNODB COMMENT="用户信息表";


# 好友表
DROP TABLE IF EXISTS friend;
CREATE TABLE friend(
	`userId` INT(11) NOT NULL COMMENT '用户编号', #非空
	`friendId` INT(11) NOT NULL COMMENT '好友用户编号', #非空
	`friendRemark` VARCHAR(10) COMMENT '好友备注', 
	`relationshipStatus` TINYINT NOT NULL DEFAULT 0 COMMENT '好友关系' #默认为0（0：普通好友，1：特别关心， 2：黑名单） 
)CHARSET='UTF8' ENGINE=INNODB COMMENT="好友表";


# 聊天消息表
DROP TABLE IF EXISTS message;
CREATE TABLE message(
	`userId` INT(11) NOT NULL COMMENT '用户编号', #非空
	`friendId` INT(11) NOT NULL COMMENT '好友用户编号', #非空
	`type` TINYINT NOT NULL DEFAULT 0 COMMENT '发送or接收', #消息类型（0：发送，1：接收） 
	`messageContent` VARCHAR(255) NOT NULL COMMENT '消息内容', #非空
	`sendTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间' #非空，默认为现在
)CHARSET='UTF8' ENGINE=INNODB COMMENT="聊天消息表";


# 运动类别表
DROP TABLE IF EXISTS category;
CREATE TABLE category(
	`id` INT(4) NOT NULL COMMENT '类别编号' PRIMARY KEY AUTO_INCREMENT, #编号主键，标识列
	`name` VARCHAR(10) NOT NULL COMMENT '类别名称'	
)CHARSET='UTF8' ENGINE=INNODB COMMENT="运动类别表";


# 运动种类表
DROP TABLE IF EXISTS sports_type;
CREATE TABLE sports_type(
	`typeId` INT(4) NOT NULL COMMENT '种类编号' PRIMARY KEY AUTO_INCREMENT, #编号主键，标识列
	`typeName` VARCHAR(10) NOT NULL COMMENT '种类名称', #非空
	`categoryId` INT(4) NOT NULL COMMENT '运动所属类别编号' #外键，所属的运动种类
)CHARSET='UTF8' ENGINE=INNODB COMMENT="运动种类表";


# 场馆表
DROP TABLE IF EXISTS stadium_info;
CREATE TABLE stadium_info(
	`stadiumId` INT(11) NOT NULL COMMENT '场馆编号' PRIMARY KEY AUTO_INCREMENT, #非空 主键，标识列
	`stadiumName` VARCHAR(20) NOT NULL COMMENT '场馆名称', #非空
	`contact` VARCHAR(20) NOT NULL COMMENT '场馆联系方式',  #非空
	`avgConsumption` DECIMAL(5,1) UNSIGNED NOT NULL DEFAULT '50' COMMENT '均消费', #非空，允许0-9999.9
	`description` VARCHAR(255) COMMENT '场馆描述',
	`headIcon` LONGBLOB COMMENT '场馆头像',
	`latitude` DOUBLE NOT NULL COMMENT '纬度', #非空
	`longitude` DOUBLE NOT NULL COMMENT '经度', #非空
	`province` VARCHAR(10) COMMENT '省',
	`city` VARCHAR(10) COMMENT '市',
	`district` VARCHAR(10) COMMENT '区',
	`street` VARCHAR(10) COMMENT '街',
	`streetNumber` VARCHAR(10) COMMENT '街号',
	`sportsTypeId` INT(4) NOT NULL COMMENT '场馆所属运动类型编号', #外键，引用自sports_type表
	`stadiumStatus` TINYINT NOT NULL DEFAULT 0 COMMENT '场馆状态' #tinyint数据类型，0：活跃；1：关闭
)CHARSET='UTF8' ENGINE=INNODB COMMENT="场馆表";


# 订单表'
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`(
	`orderId` INT(11) NOT NULL COMMENT '订单编号' PRIMARY KEY AUTO_INCREMENT, #非空 主键，标识列
	`orderName` VARCHAR(10) NOT NULL COMMENT '订单名称', #非空
	`orderExpectedSize` INT(3) NOT NULL COMMENT '组团开启人数', #非空
	`orderLaunchTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间', #发布时间，默认为当前时间
	`orderAppointTime` TIMESTAMP NOT NULL COMMENT '约定时间', #约定时间，程序中需限定约定时间必须在发布时间之后
	`orderStatus` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态', #tinyint数据类型，0：组团发起；1：组团成功；2：已结束；3：已取消
	`latitude` DOUBLE NOT NULL COMMENT '纬度', #非空
	`longitude` DOUBLE NOT NULL COMMENT '经度', #非空
	`contact` VARCHAR(20) NOT NULL COMMENT '订单联系方式', #非空
	`orderLocation` VARCHAR(50) COMMENT '订单的约定地点（自定义）', 
	
	`sportsTypeId` INT(4) NOT NULL COMMENT '组团运动种类编号', #外键 引自运动种类表 
	`orderSponsorId` INT(11) NOT NULL COMMENT '订单发起人编号', #外键 引自用户信息表
	`stadiumId` INT(11) COMMENT '场馆编号' #外键 引自场馆信息表
)CHARSET='UTF8' ENGINE=INNODB COMMENT="订单表";

# 订单参加记录表
DROP TABLE IF EXISTS `order_attendance_record`;
CREATE TABLE `order_attendance_record`(
	`orderId` INT(11) NOT NULL COMMENT '订单编号', #外键 引自订单表
	`participantId` INT(11) NOT NULL COMMENT '参与人编号', #外键 引自用户信息表
	PRIMARY KEY(`orderId`,`participantId`)
)CHARSET='UTF8' ENGINE=INNODB COMMENT="订单参加记录表";


# 建立表间关系

# 修改表
ALTER TABLE `stadium_info` CHANGE `longtitude` `longitude` DOUBLE NOT NULL COMMENT '经度';


# 插入初始数据

# 用户登录表的初始三个用户
INSERT INTO `user_login`(`userPhone`,`userPassword`) VALUES('17620388542','123456'),('13741234567','123456'),('13912345678','123456');
# 用户信息表的初始
INSERT INTO `user_info`(`userPhone`,`userNickName`,`userBirthday`,`userGender`) VALUES
('17620388542','Noah','2000-11-15',DEFAULT), ('13741234567','鹏鹏','1988-12-15',DEFAULT),('13912345678','Tina','1999-02-21','女');
# 建立初始用户间friend关系
INSERT INTO `friend`(`userId`,`friendId`) VALUES
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13741234567')),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13741234567'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542')),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678')),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542')),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13741234567'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678')),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13741234567'));
# 创建Noah和Tina间的聊天记录
INSERT INTO `message`(`userId`,`friendId`,`type`,`messageContent`,`sendTime`) VALUES 
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), 0, 'hello', '20190617140830'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), 1, 'hello', '20190617140830'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), 0, 'hi', '20190617140900'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), 1, 'hi', '20190617140900'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), 0, '你想不想去SHE的“十八”呀！！！', '20190803000200'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), 1, '你想不想去SHE的“十八”呀！！！', '20190803000200'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), 0, '我好想去啊！', '20190803000215'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), 1, '我好想去啊！', '20190803000215'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), 0, '我都不在国内怎么去......', '20190803080215'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), 1, '我都不在国内怎么去......', '20190803080215'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), 0, '到时候我也得出国耶，哭了', '20190804122015'),
((SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'), 1, '到时候我也得出国耶，哭了', '20190804122015');
# 运动类别
INSERT INTO `category`(`name`) VALUES('球类'),('健身'),('有氧'),('无氧'),('娱乐'),('柔和');
# 运动种类
INSERT INTO `sports_type`(`typeName`,`categoryId`) VALUES 
('足球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('篮球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('排球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('手球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('棒球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('乒乓球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('羽毛球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('网球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('高尔夫球',(SELECT `id` FROM `category` WHERE `name`='球类')),
('健身',(SELECT `id` FROM `category` WHERE `name`='健身')),
('跑步',(SELECT `id` FROM `category` WHERE `name`='有氧')),
('自行车骑行',(SELECT `id` FROM `category` WHERE `name`='有氧')),
('瑜伽',(SELECT `id` FROM `category` WHERE `name`='柔和'));
# 初始场馆（测试）
TRUNCATE TABLE `stadium_info`;
INSERT INTO `stadium_info`(`stadiumName`,`contact`,`avgConsumption`,`description`,`latitude`,`longitude`,`province`,`city`,`district`,`street`,`streetNumber`,`sportsTypeId`,`stadiumStatus`) VALUES
('泥岗社区公园','', '0','公共运动社区，周末节假日人比较多','22.575146','114.105508','广东省','深圳市','罗湖区','金碧路','16号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='篮球'),0),
('深圳市凤光小学','075582423696', '0','学校内足球场','22.575534','114.106742','广东省','深圳市','罗湖区','泥岗西路','40号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='足球'),1),
('深圳体育馆篮球训练基地游泳馆分部','', '50','为篮球训练基地，场地不定开放','22.562973','114.09504','广东省','深圳市','福田区','笋岗西路','2006号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='篮球'),0),
('华富村社区-乒乓羽毛球场','', '0','乒乓球和羽毛球的共同场地','22.562051','114.088307','广东省','深圳市','福田区','华富一街','',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='羽毛球'),0),
('深圳中心公园篮球场','', '10','中心公园有公司包场和散场两种场地类型','22.552041','114.080936','广东省','深圳市','福田区','振华西路','35号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='篮球'),0),
('深圳中心公园足球场','', '30','中心公园内场地设施赞','22.552122','114.082044','广东省','深圳市','福田区','振华西路','35号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='足球'),0),
('香蜜湖体育中心','075582722250', '10','场地设施极佳，提供约场服务','22.558441','114.038406','广东省','深圳市','福田区','侨香路','1001号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='篮球'),0);
# 测试订单
INSERT INTO `order`(`orderName`,`orderExpectedSize`,`orderLaunchTime`,`orderAppointTime`,`orderStatus`,`latitude`,`longitude`,`contact`,`orderLocation`,`sportsTypeId`,`orderSponsorId`,`stadiumId`) VALUES
('周末养生足球团', '22', DEFAULT, '20190810193000',DEFAULT,'22.552122','114.082044','17620388542','振华四路35号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='足球'),(SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'),(SELECT `stadiumId` FROM `stadium_info` WHERE `stadiumName`='深圳中心公园足球场')),
('周末养生足球团', '22', '20190801163020', '`category``category`20190803193000','3','22.552122','114.082044','17620388542','振华四路35号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='足球'),(SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'),(SELECT `stadiumId` FROM `stadium_info` WHERE `stadiumName`='深圳中心公园足球场')),
('羽毛球，约吗？', '2', DEFAULT, '20190810193000','1','22.562051','114.088307','17620388542','华富一街',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='羽毛球'),(SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542'),(SELECT `stadiumId` FROM `stadium_info` WHERE `stadiumName`='华富村社区-乒乓羽毛球场')),
('泥岗青年篮球', '3', '20190801193000', '20190802203000','2','22.575146','114.105508','13741234567','金碧路16号',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='篮球'),(SELECT `userId` FROM `user_info` WHERE `userPhone`='13741234567'),(SELECT `stadiumId` FROM `stadium_info` WHERE `stadiumName`='泥岗社区公园')),
('找个女生打羽毛球', '2', DEFAULT, '20190810193000','0','22.562051','114.088307','13912345678','华富一街',(SELECT `typeId` FROM `sports_type` WHERE `typeName`='羽毛球'),(SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'),(SELECT `stadiumId` FROM `stadium_info` WHERE `stadiumName`='华富村社区-乒乓羽毛球场'));
# 设置参与测试订单
INSERT INTO `order_attendance_record`(`orderId`,`participantId`) VALUES
((SELECT `orderId` FROM `order` WHERE `orderName`='周末养生足球团' AND `orderAppointTime`='20190810193000'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542')),
((SELECT `orderId` FROM `order` WHERE `orderName`='周末养生足球团' AND `orderAppointTime`='20190803193000'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542')),
((SELECT `orderId` FROM `order` WHERE `orderName`='羽毛球，约吗？'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542')),
((SELECT `orderId` FROM `order` WHERE `orderName`='羽毛球，约吗？'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13741234567')),
((SELECT `orderId` FROM `order` WHERE `orderName`='泥岗青年篮球'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13741234567')),
((SELECT `orderId` FROM `order` WHERE `orderName`='泥岗青年篮球'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='17620388542')),
((SELECT `orderId` FROM `order` WHERE `orderName`='泥岗青年篮球'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678')),
((SELECT `orderId` FROM `order` WHERE `orderName`='找个女生打羽毛球'), (SELECT `userId` FROM `user_info` WHERE `userPhone`='13912345678'));

# 查询
SELECT * FROM `order`;
SELECT * FROM `stadium_info`;