drop table if exists "MONSTER" CASCADE;
drop table if exists "BATTLE" CASCADE;
create sequence IF NOT EXISTS hibernate_sequence start with 4 increment by 1;

CREATE TABLE "MONSTER" (
    "ID" bigint not null,     
    "ATTACK" bigint not null,
    "DEFENSE" bigint not null,
    "HP" bigint not null,
    "SPEED" bigint not null,
    "IMAGE_URL" varchar(255) not null,
     primary key ("ID")
);

create table  "BATTLE" (
    "ID" bigint not null, 
    "MONSTER_A_ID" bigint not null, 
    "MONSTER_B_ID" bigint not null, 
    "MONSTER_WINNER" bigint not null,     
     primary key ("ID")
);

alter table "BATTLE" add constraint "FK_MONSTER_1" foreign key ("MONSTER_A_ID") references "MONSTER";
alter table "BATTLE" add constraint "FK_MONSTER_2" foreign key ("MONSTER_B_ID") references "MONSTER";
alter table "BATTLE" add constraint "FK_MONSTER_3" foreign key ("MONSTER_WINNER") references "MONSTER";

insert into "MONSTER"("ID", "ATTACK", "DEFENSE", "HP", "SPEED", "IMAGE_URL") values(1, 50, 40, 30, 25, 'image_url1');
insert into "MONSTER"("ID", "ATTACK", "DEFENSE", "HP", "SPEED", "IMAGE_URL")  values(2, 30, 10, 20, 15, 'image_url2');

insert into "BATTLE"("ID", "MONSTER_A_ID", "MONSTER_B_ID", "MONSTER_WINNER") values(1, 1, 2, 1);