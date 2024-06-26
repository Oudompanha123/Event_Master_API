-- Script for event_master_project
create table organization
(
    org_id   serial primary key,
    org_name varchar(40)  default 'No Name',
    code     varchar(6) not null,
    address  varchar(255) default 'No Address',
    logo     text
);

create table member
(
    member_id     serial primary key,
    member_name   varchar(40) not null,
    gender        varchar(6),
    phone         varchar(11) not null,
    email         varchar(40) not null,
    password      text        not null,
    address       varchar(255) default 'No Address',
    picture       text,
    date_of_birth date,
    role          varchar(15) not null,
    is_approve    boolean      default false,
    org_id        integer     not null,
    constraint org_id_member_fk
        foreign key (org_id)
            references organization (org_id)
);

create table member_history(
                               id serial primary key,
                               member_id integer,
                               member_name varchar(40),
                               org_id integer,
                               role varchar(15),
                               picture text,
                               email varchar(40),
                               phone varchar(11)
);

create table asset
(
    asset_id   serial primary key,
    asset_name varchar(40) not null,
    qty        decimal(6, 2)   not null,
    unit       varchar(20) not null,
    created_at   timestamp    default current_timestamp,
    update_at   timestamp    default current_timestamp,
    org_id     integer     not null,
    constraint org_id_asset_fk
        foreign key (org_id)
            references organization (org_id)
);

create table category
(
    cate_id   serial primary key,
    cate_name varchar(40) not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    update_at timestamp default CURRENT_TIMESTAMP,
    created_by integer not null,
    org_id    integer     not null,
    constraint org_id_category_fk
        foreign key (org_id)
            references organization (org_id),
    constraint create_by_category_fk
        foreign key (created_by)
            references member (member_id)
);


create table event
(
    event_id     serial primary key,
    event_name   varchar(60) not null,
    start_date   timestamp   not null,
    end_date     timestamp   not null,
    duration     varchar(30),
    address      varchar(255),
    poster       text,
    description  varchar(255),
    is_open      boolean default true, -- by default it is open
    is_post      boolean default false,
    max_attendee integer     not null,
    registration_form jsonb,
    created_at   timestamp    default current_timestamp,
    cate_id      integer     not null,
    org_id       integer     not null,
    constraint cate_id_event_fk
        foreign key (cate_id)
            references category (cate_id),
    constraint org_id_event_fk
        foreign key (org_id)
            references organization (org_id)
);

create table attendee
(
    attendee_id serial primary key,
    data        jsonb   not null,
    status boolean default false, -- true attendee join event
    event_id    integer not null,
    constraint event_id_attendee_fk
        foreign key (event_id)
            references event (event_id)
            ON DELETE CASCADE ON UPDATE CASCADE
);



create table material
(
    material_id   serial primary key,
    material_name varchar(50) not null,
    qty           decimal(6, 2) not null,
    to_get        decimal(6, 2) not null,
    unit          varchar(20) not null,
    remark        text,
    status        varchar(10) not null default 'pending',
    assign_date   timestamp   not null default CURRENT_TIMESTAMP,
    due_date      timestamp,
    handler_id    integer, -- find member or member_history table
    supporters    jsonb, -- have many supporters
    event_id      integer     not null,
    constraint event_id_material_fk
        foreign key (event_id)
            references event (event_id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

create table agenda
(
    agenda_id          serial primary key,
    data               jsonb,
    event_id           integer     not null,
    constraint event_id_agenda_fk
        foreign key (event_id)
            references event (event_id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

create table otp
(
    otp_id     serial primary key,
    otp_code   varchar(4),
    issued_at  timestamp default CURRENT_TIMESTAMP,
    expiration timestamp,
    is_verify     boolean   default false,
    member_id  int not null,
    constraint member_id_otp_fk
        foreign key (member_id)
            references member (member_id)
            ON DELETE CASCADE
);