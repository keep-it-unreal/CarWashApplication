insert into city(name)
    values ('Москва'),
           ('Воронеж'),
           ('Саратов');

insert into user_info(name,phone,address_mail,password,role,city_id_city)
    values ('owner1','8-910-001-01-01','owner1@gmail.com','123',2,1),
           ('owner2','8-910-001-01-02','owner2@gmail.com','123',2,1),
           ('owner3','8-910-001-01-03','owner3@gmail.com','123',2,1),
           ('owner4','8-910-001-01-04','owner4@gmail.com','123',2,2),
           ('owner5','8-910-001-01-05','owner5@gmail.com','123',2,2),
           ('owner6','8-910-001-01-06','owner6@gmail.com','123',2,3),
           ('owner7','8-910-001-01-07','owner7@gmail.com','123',2,3),
           ('owner8','8-910-001-01-08','owner8@gmail.com','123',2,3),
           ('user01','8-910-001-02-01','user01@gmail.com','123',1,1),
           ('user02','8-910-001-02-02','user02@gmail.com','123',1,2);

insert into cause_interrupt(name)
    values ('Авария'),
           ('Проверка из пожарной'),
           ('Ремонт');

insert into car_wash(user_info_id_user,city_id_city,address,latitude,longitude,daily_start_time,daily_end_time,price)
    values (1,1,'ул. Брянский Пост, 6, стр. 1Б',55.77348,37.533417,'06','23',200),
           (1,1,'6-я Радиальная ул., 22к1с4',55.390032,37.891846,'00','23',180),
           (1,1,'просп. Мира, вл94с1Б',55.869918,37.585602,'05','22',250),
           (1,1,'Большая Семёновская улица',55.766314,37.608727,'06','23',230),
           (2,1,'ул. Брянский Пост, 5А',55.766314,37.608727,'00','23',210),
           (2,1,'1-я Рыбинская ул., 3А',55.766314,37.608727,'05','23',180),
           (2,1,'Нижегородская ул., 29-33с9',55.766314,37.608727,'06','23',240),
           (3,1,'улица Буженинова, 51с2',55.766314,37.608727,'00','23',280),
           (4,2,'ул. Рязанская, 117',51.730856,39.181366,'00','23',180),
           (4,2,'ул. Урицкого, 48А',51.715543,39.212952,'00','23',190),
           (4,2,'Ленинградская ул., 56/1',51.653815,39.26651,'05','22',200),
           (4,2,'Московский просп., 102В · 8',51.712565,39.16214,'06','23',160),
           (5,2,'улица Карла Маркса, 72А/2',51.640607,39.204025,'00','23',220),
           (6,3,'ул. Чернышевского, 109',51.406059,45.865173,'00','23',160),
           (7,3,'ул. Чапаева, 196 · 8',51.442025,45.936584,'06','22',150),
           (8,3,'ул. Радищева, 65',51.429183,45.967484,'05','23',170);

insert into work_shedule(car_wash_id,monday ,tuesday ,wednesday ,thursday ,friday ,saturday ,sunday )
    values (1,0,0,0,0,0,0,0),
	   (2,0,0,0,1,0,0,0),
	   (3,0,0,0,0,0,0,0),
	   (4,1,0,0,0,0,0,0),
	   (5,0,1,0,0,0,0,0),
	   (6,0,0,1,0,0,0,0),
	   (7,0,0,0,0,0,0,0),
	   (8,0,0,0,0,1,0,0),
	   (9,0,0,0,0,0,1,0),
	   (10,1,0,0,0,0,0,0),
	   (11,0,1,0,0,0,0,0),
	   (12,0,0,1,0,0,0,0),
	   (13,0,0,0,1,0,0,0),
	   (14,0,0,0,0,1,0,0),
	   (15,0,0,0,0,0,1,0),
	   (16,1,0,0,0,0,0,0);


