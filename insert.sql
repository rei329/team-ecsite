SET foreign_key_checks=1;

USE peppermilldb;

INSERT INTO mst_user
(user_name, password, family_name, first_name, family_name_kana, first_name_kana, gender)
VALUES ('yamada@gmail.com', '111111', '山田', '太郎', 'やまだ', 'たろう', 0);

INSERT INTO mst_category (category_name,category_description)VALUES
('トップス', 'トップスのカテゴリーです'),
('ボトムス', 'ボトムスのカテゴリーです'),
('シューズ', 'シューズのカテゴリーです');

INSERT INTO mst_product(product_name,product_name_kana,product_description,category_id,price,image_full_path,release_date,release_company)VALUES 
('Ariats Shirt','ありあっとずしゃつ','サンフランシスコのブランドより発売',1,950,'/img/tops/tshirt.png','2023/02/05','トップロータス'),
('Heat Knitted Cap','ひーとにってっどきゃっぷ','ロンドンのブランドより発売',1,1450,'/img/tops/knit.jpg','2023/02/15','ポールウエストウッド'),
('Sweat à Capuche','すぅえと・あ・かぷーしゅ','パリのブランドより発売',1,2950,'/img/tops/hoodie.jpg','2023/02/25','レ・ボン・ヴォルテール'),
('Denim de Milan','でにむ・で・みらん','ミラノのブランドより発売',2,1950,'/img/bottoms/jeans.jpg','2023/03/05','ビッフィフェリー'),
('Slacks of Vicotoria','すらっくす・おぶ・びくとりあ','ロンドンのブランドより発売',2,2950,'/img/bottoms/slacks.jpg','2023/03/15','ポールウエストウッド'),
('Power Sneakers','ぱわーすにーかーず','サンフランシスコのブランドより発売',3,3950,'/img/shoes/sneakers.jpg','2023/03/25','トップロータス'),
('Bottes d Expiation','ばとえす・だ・えくすぴえいしゃん','パリのブランドより発売',3,4950,'/img/shoes/boots.jpg','2023/04/05','レ・ボン・ヴォルテール'),
('Sandalo da Mare','さんだろう・だ・まーれい','ミラノのブランドより発売',3,950,'/img/shoes/sandals.jpg','2023/04/15','ビッフィフェリー');
