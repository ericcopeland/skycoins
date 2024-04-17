insert into product_entry(id, create_date, buy_price, sell_price, buy_volume, sell_volume, buy_orders, sell_orders, buy_moving_week, sell_moving_week) values(uuid(), current_timestamp(), 10, 5, 10, 5, 100, 50, 500, 250);
insert into product_entry(id, create_date, buy_price, sell_price, buy_volume, sell_volume, buy_orders, sell_orders, buy_moving_week, sell_moving_week) values(uuid(), current_timestamp(), 10, 5, 10, 5, 100, 50, 500, 250);
insert into product_entry(id, create_date, buy_price, sell_price, buy_volume, sell_volume, buy_orders, sell_orders, buy_moving_week, sell_moving_week) values(uuid(), current_timestamp(), 10, 5, 10, 5, 100, 50, 500, 250);
insert into product_entry(id, create_date, buy_price, sell_price, buy_volume, sell_volume, buy_orders, sell_orders, buy_moving_week, sell_moving_week) values(uuid(), current_timestamp(), 10, 5, 10, 5, 100, 50, 500, 250);
insert into product_entry(id, create_date, buy_price, sell_price, buy_volume, sell_volume, buy_orders, sell_orders, buy_moving_week, sell_moving_week) values(uuid(), current_timestamp(), 10, 5, 10, 5, 100, 50, 500, 250);

insert into product(id, create_date, item_id, item_name) values(uuid(), current_timestamp(), 'test_id_1', 'test_name_1');
insert into product(id, create_date, item_id, item_name) values(uuid(), current_timestamp(), 'test_id_2', 'test_name_2');
insert into product(id, create_date, item_id, item_name) values(uuid(), current_timestamp(), 'test_id_3', 'test_name_3');

update product_entry set product_id = (select id from product limit 1);

