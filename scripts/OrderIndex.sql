LOAD DATA INFILE '/var/lib/mysql-files/dummy_orders3.csv'   #더미 데이터 추가
    INTO TABLE orders
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (id, created_at, modified_at, b2bmember_id, delivery_status, name, order_status, quantity, total_price, tracking_number, b2cmember_id, product_id);


CREATE INDEX idx_created_at ON orders(created_at);  # created_at 인덱스 추가

SHOW INDEX FROM orders;     # 추가 된 인덱스 확인


EXPLAIN select        # 주문 조회 실행계획 확인 쿼리
    o1_0.id,
    o1_0.b2bmember_id,
    o1_0.b2cmember_id,
    o1_0.created_at,
    o1_0.delivery_status,
    o1_0.modified_at,
    o1_0.name,
    o1_0.order_status,
    o1_0.product_id,
    o1_0.quantity,
    o1_0.total_price,
    o1_0.tracking_number
from
    orders o1_0
where
    o1_0.b2cmember_id = 1
order by
    o1_0.created_at desc
limit
    10;


