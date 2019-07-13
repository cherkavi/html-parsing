-- Session.one session, get information about one attempt of parsing 
SELECT * FROM parser_session WHERE id =1 OR session_id =1 ORDER BY id;

-- Human.get by id
select * from human where id=9;

-- Human.description, parameters, services, phones, images
select * from human_* where human_id=9;

-- clear all data:
delete from parser_session;
delete from human_services;
delete from human_prices;
delete from human_phones;
delete from human_description;
delete from human_images;
delete from human;
