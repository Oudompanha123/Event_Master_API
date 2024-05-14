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
    role          varchar(10) not null,
    is_approve    boolean      default false,
    org_id        integer     not null,
    constraint org_id_member_fk
        foreign key (org_id)
            references organization (org_id)
);

create table asset
(
    asset_id   serial primary key,
    asset_name varchar(40) not null,
    qty        decimal(6, 2)   not null,
    unit       varchar(20) not null,
    add_date   date        not null default current_date,
    org_id     integer     not null,
    constraint org_id_asset_fk
        foreign key (org_id)
            references organization (org_id)
);

create table category
(
    cate_id   serial primary key,
    cate_name varchar(40) not null,
    org_id    integer     not null,
    constraint org_id_category_fk
        foreign key (org_id)
            references organization (org_id)
);

create table registration_form
(
    form_id   serial primary key,
    form_name varchar(50) not null,
    data      jsonb,
    org_id    integer     not null,
    cate_id   integer     not null,
    constraint org_id_registration_form_fk
        foreign key (org_id)
            references organization (org_id),
    constraint cate_id_registration_form_fk
        foreign key (cate_id)
            references category (cate_id)
);

create table event
(
    event_id     serial primary key,
    event_name   varchar(60) not null,
    start_date   timestamp   not null,
    end_date     timestamp   not null,
    duration     varchar(30),
    poster       text,
    description  varchar(255),
    status       boolean default true, -- by default it is open
    max_attendee integer     not null,
    is_post      boolean default false,
    cate_id      integer     not null,
    form_id      integer     not null,
    org_id       integer     not null,
    constraint cate_id_event_fk
        foreign key (cate_id)
            references category (cate_id),
    constraint form_id_event_fk
        foreign key (form_id)
            references registration_form (form_id),
    constraint org_id_event_fk
        foreign key (org_id)
            references organization (org_id)
);

create table attendee
(
    attendee_id serial primary key,
    data        jsonb   not null,
    event_id    integer not null,
    constraint event_id_attendee_fk
        foreign key (event_id)
            references event (event_id)
);

create table material
(
    material_id   serial primary key,
    material_name varchar(50) not null,
    qty           decimal(6, 2) not null,
    unit          varchar(20) not null,
    remark        text,
    status        varchar(10) not null default 'pending',
    assign_date   timestamp   not null default CURRENT_TIMESTAMP,
    due_date      timestamp   not null,
    handler_id    integer     not null,
    supporters    jsonb,
    event_id      integer     not null,
    constraint handler_id_material_fk
        foreign key (handler_id)
            references member (member_id),
    constraint event_id_material_fk
        foreign key (event_id)
            references event (event_id)
);

create table agenda
(
    agenda_id          serial primary key,
    data               jsonb,
    event_id           integer     not null,
    constraint event_id_agenda_fk
        foreign key (event_id)
            references event (event_id)
);

create table otp
(
    otp_id     serial primary key,
    otp_code   varchar(4),
    issued_at  timestamp default CURRENT_TIMESTAMP,
    expiration timestamp,
    verify     boolean   default false,
    member_id  int not null,
    constraint member_id_otp_fk
        foreign key (member_id)
            references member (member_id)
);