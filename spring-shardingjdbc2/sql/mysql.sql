
CREATE TABLE t_user(
  user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255) NOT NULL ,
  password VARCHAR(255) NOT NULL ,
  phone VARCHAR(255) NOT NULL
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;


CREATE TABLE t_cust(
  cust_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  cust_name VARCHAR(255)
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;


CREATE TABLE t_product(
  prod_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  prod_name VARCHAR(255)
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;


CREATE TABLE t_order(
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INT,
  prod_id INT,
  user_id INT,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INT
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_order_0 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INT,
  prod_id INT,
  user_id INT,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INT
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_order_1 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INT,
  prod_id INT,
  user_id INT,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INT
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_order_2 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INT,
  prod_id INT,
  user_id INT,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INT
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_order_3 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INT,
  prod_id INT,
  user_id INT,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INT
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;