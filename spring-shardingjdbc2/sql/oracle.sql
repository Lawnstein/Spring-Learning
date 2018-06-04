
CREATE TABLE t_user(
  user_id INTEGER NOT NULL PRIMARY KEY ,
  user_name VARCHAR(255) NOT NULL ,
  password VARCHAR(255) NOT NULL ,
  phone VARCHAR(255) NOT NULL
) ;


CREATE TABLE t_cust(
  cust_id INTEGER NOT NULL PRIMARY KEY ,
  cust_name VARCHAR(255)
) ;


CREATE TABLE t_product(
  prod_id INTEGER NOT NULL PRIMARY KEY ,
  prod_name VARCHAR(255)
) ;


CREATE TABLE t_order(
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INTEGER,
  prod_id INTEGER,
  user_id INTEGER,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INTEGER
) ;

CREATE TABLE t_order_0 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INTEGER,
  prod_id INTEGER,
  user_id INTEGER,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INTEGER
) ;

CREATE TABLE t_order_1 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INTEGER,
  prod_id INTEGER,
  user_id INTEGER,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INTEGER
) ;

CREATE TABLE t_order_2 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INTEGER,
  prod_id INTEGER,
  user_id INTEGER,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INTEGER
) ;

CREATE TABLE t_order_3 (
  order_id VARCHAR(100) NOT NULL PRIMARY KEY ,
  cust_id INTEGER,
  prod_id INTEGER,
  user_id INTEGER,
  amount DECIMAL,
  remark VARCHAR(255),
  sharding_key INTEGER
) ;

CREATE SEQUENCE userid_sequence INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE;
CREATE TRIGGER userid_trigger BEFORE
INSERT ON t_user FOR EACH ROW WHEN (new.user_id is null)
BEGIN 
        select userid_sequence.nextval into:new.user_id from dual;
END;

CREATE SEQUENCE custid_sequence INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE;
CREATE TRIGGER custid_trigger BEFORE
INSERT ON t_cust FOR EACH ROW WHEN (new.cust_id is null)
BEGIN 
         select custid_sequence.nextval into:new.cust_id from dual;
END;

CREATE SEQUENCE prodid_sequence INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE;
CREATE TRIGGER prodid_trigger BEFORE
INSERT ON t_product FOR EACH ROW WHEN (new.prod_id is null)
BEGIN 
         select prodid_sequence.nextval into:new.prod_id from dual;
END;

