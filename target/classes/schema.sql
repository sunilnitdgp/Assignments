
DROP TABLE MEDICINES_DETAILS IF  EXISTS;
DROP TABLE ORDERS IF EXISTS;
DROP TABLE ORDERED_ITEMS IF EXISTS;

CREATE TABLE MEDICINES_DETAILS(
c_unique_id number AUTO_INCREMENT PRIMARY KEY,
c_name varchar2(1000) NOT NULL,
c_batch_no varchar2(100) NOT NULL,
d_expiry_date date NOT NULL,
n_balance_qty number NOT NULL,
c_packaging varchar2(100) NOT NULL,
c_unique_code varchar2(100) NOT NULL,
c_schemes varchar2(20),
n_mrp number NOT NULL,
c_manufacturer varchar2(200) NOT NULL,
hsn_code number NOT NULL,
created_on date DEFAULT SYSDATE NOT NULL
);

CREATE INDEX med_name ON MEDICINES_DETAILS(c_name);

CREATE TABLE ORDERS(
unique_order_id number AUTO_INCREMENT PRIMARY KEY,
created_on date DEFAULT SYSDATE NOT NULL
);

CREATE TABLE ORDERED_ITEMS (
unique_ordered_item_id number AUTO_INCREMENT PRIMARY KEY,
unique_order_id number NOT NULL,
quantity number NOT NULL,
created_on date DEFAULT SYSDATE NOT NULL,
c_unique_id number NOT NULL,
c_name varchar2(1000) NOT NULL,
CONSTRAINT fk_order_item_det FOREIGN KEY (c_unique_id) REFERENCES MEDICINES_DETAILS(c_unique_id),
CONSTRAINT fk_ordered_item_order FOREIGN KEY (unique_order_id) REFERENCES ORDERS(unique_order_id)
);

