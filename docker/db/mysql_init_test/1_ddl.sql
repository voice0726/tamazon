create table address
(
    uuid         varchar(36)                         not null,
    user_id      varchar(36)                         not null,
    postal_code  varchar(1024)                       not null,
    address1     varchar(1024)                       not null,
    address2     varchar(1024)                       null,
    address3     varchar(1024)                       null,
    default_addr tinyint                             not null,
    deleted      tinyint                             not null,
    created_at   timestamp default CURRENT_TIMESTAMP not null,
    updated_at   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint address_uuid_uindex
        unique (uuid)
);

create index address_user_id_index
    on address (user_id);

alter table address
    add primary key (uuid);


create table cart
(
    uuid       varchar(36)                         not null,
    user_id    varchar(36)                         not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint cart_uuid_uindex
        unique (uuid)
);

create index cart_user_id_index
    on cart (user_id);

alter table cart
    add primary key (uuid);


create table cart_item
(
    uuid       varchar(36)                         not null,
    cart_id    varchar(36)                         not null,
    item_id    varchar(36)                         not null,
    count      int                                 not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint cart_item_uuid_uindex
        unique (uuid)
);

create index cart_item_cart_id_item_id_index
    on cart_item (cart_id, item_id);

alter table cart_item
    add primary key (uuid);


create table category
(
    uuid       varchar(36)                         not null
        primary key,
    name       varchar(1024)                       not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);


create table item
(
    uuid        varchar(36)                         not null,
    name        varchar(1024)                       not null,
    category_id varchar(36)                         not null,
    description varchar(1024)                       not null,
    price       int                                 not null,
    stock       bigint                              not null,
    deleted     tinyint                             not null,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    updated_at  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint item_uuid_uindex
        unique (uuid)
);

alter table item
    add primary key (uuid);


create table `order`
(
    uuid          varchar(36)                         not null
        primary key,
    user_id       varchar(36)                         not null,
    pay_method_id varchar(36)                         not null,
    ship_status   smallint  default 0                 not null,
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    updated_at    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);


create table order_item
(
    uuid       varchar(36)                         not null
        primary key,
    order_id   varchar(36)                         not null,
    item_id    varchar(36)                         not null,
    count      int       default 1                 not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);


create table pay_method
(
    uuid           varchar(36)                         not null
        primary key,
    user_id        varchar(36)                         not null,
    brand          smallint                            not null,
    card_number    varchar(255)                        not null,
    card_holder    varchar(1024)                       not null,
    expire_month   int                                 not null,
    expire_year    int                                 not null,
    default_method tinyint                             not null,
    deleted        tinyint                             not null,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    updated_at     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);


create table review
(
    uuid       varchar(36)                         not null
        primary key,
    user_id    char(36)                            not null,
    item_id    char(36)                            not null,
    star       smallint                            not null,
    comment    varchar(1024)                       not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);


create table role
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create table user
(
    uuid       varchar(36)                         not null
        primary key,
    username   varchar(255)                        not null,
    view_name  varchar(255)                        not null,
    password   varchar(255)                        not null,
    role_id    int       default 0                 not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint user_username_uindex
        unique (username)
);

