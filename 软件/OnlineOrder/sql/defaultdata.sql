
###################################################
#########      等级数据			  #################
###################################################
insert into `t_rank` values(1,1,'游客'),
						(2,2,'普通用户'),
						(3,3,'高级用户'),
						(4,4,'特殊用户'),
						(5,5,'管理员'),
						(6,6,'超级管理员');


###################################################
#########      企业表数据			  #################
###################################################
insert into `t_enterprise_info` values
  (2,'00000000000000000001','京都川菜馆','abcd','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (3,'00013000000000000001','京都川菜馆','abcd','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (4,'23000000000000000001','s四川川菜馆','rose','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (5,'01234000000000000001','天津狗不理','jack','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (6,'01234567000000000001','北京烤鸭店','jion','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (7,'00000000000232323001','南京驴肉火烧','da.shan','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (8,'00000000015236000001','陕西家乡菜','max','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (9,'00052100000000000001','山西刀削面','banana','13800000000','天安门广场北侧路边小胡同','12345679@163.com'),
  (10,'14500000000000000001','湖南烧刀子','potato','13800000000','天安门广场北侧路边小胡同','12345679@163.com');

###################################################
#########      菜品分类数据			  #################
###################################################
insert into `t_item_type` values
  (1,1,'热菜'),
  (2,2,'凉菜'),
  (3,3,'汤羹'),
  (4,4,'主食'),
  (5,5,'小吃'),
  (6,6,'家常菜'),
  (7,7,'跑酱腌菜'),
  (8,8,'西餐'),
  (9,9,'烘焙'),
  (10,10,'烤箱菜'),
  (11,11,'饮品'),
  (12,12,'零食'),
  (13,13,'火锅'),
  (14,14,'自制食材'),
  (15,15,'海鲜'),
  (16,16,'宴客菜');

###################################################
#########      菜品默认数据			t_item  #################
###################################################
insert into `t_item`(enterprise_id,item_id,item_name,item_price,item_pic) values
  ('00013000000000000001','00000000000000000000','',12.5,'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3108145806,1482744700&fm=200&gp=0.jpg'),
('01234000000000000001','00000000000000000000','',23.5,'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524224424600&di=0797c2a358841ada6bed3c7991c76ca2&imgtype=0&src=http%3A%2F%2Fimage.tech-food.com%2Fimages%2Fkndata%2Fbpic%2F201412%2F20141219135916321632.jpg'),
('01234567000000000001','00000000000000000000','',123.5,'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524224424600&di=66e3e1510a4754074e0315f8c04659bc&imgtype=0&src=http%3A%2F%2Fpic113.nipic.com%2Ffile%2F20161025%2F23694910_162121619000_2.jpg'),
('00000000000232323001','00000000000000000000','',12.5,'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3108145806,1482744700&fm=200&gp=0.jpg'),
('00000000000000000000','00000000000000000000','',12.5,'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3108145806,1482744700&fm=200&gp=0.jpg');