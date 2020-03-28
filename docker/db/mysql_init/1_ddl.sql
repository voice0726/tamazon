create table address
(
    id           bigint                              not null
        primary key,
    uuid         varchar(36)                         not null,
    user_id      bigint                              not null,
    postal_code  varchar(1024)                       not null,
    address1     varchar(1024)                       not null,
    address2     varchar(1024)                       null,
    address3     varchar(1024)                       null,
    default_addr tinyint                             not null,
    deleted      tinyint                             not null,
    created_at   timestamp default CURRENT_TIMESTAMP not null,
    updated_at   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint address_id_userid_uindex
        unique (id, user_id),
    constraint address_uuid_uindex
        unique (uuid)
);


create table cart
(
    id         char(36)                            not null
        primary key,
    user_id    char(36)                            not null,
    checkout   tinyint                             not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint cart_id_user_id_uindex
        unique (id, user_id)
);


create table cart_item
(
    id         char(36)                            not null
        primary key,
    cart_id    char(36)                            not null,
    item_id    char(36)                            not null,
    count      int                                 not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint cart_item_id_cart_id_item_id_uindex
        unique (id, cart_id, item_id)
);


create table category
(
    id         bigint auto_increment,
    uuid       varchar(36)                         not null,
    name       varchar(1024)                       not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint category_id_uindex
        unique (id),
    constraint category_uuid_uindex
        unique (uuid)
);

alter table category
    add primary key (id);


create table item
(
    id          bigint auto_increment
        primary key,
    uuid        varchar(36)                         not null,
    name        varchar(1024)                       not null,
    category_id varchar(1024)                       not null,
    description varchar(1024)                       not null,
    price       int                                 not null,
    stock       bigint                              not null,
    deleted     tinyint                             not null,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    updated_at  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint item_uuid_uindex
        unique (uuid)
);


create table pay_method
(
    id          bigint auto_increment
        primary key,
    uuid        varchar(36)                         not null,
    user_id     bigint                              not null,
    brand       smallint                            not null,
    card_number int                                 not null,
    card_holder varchar(1024)                       not null,
    `default`   tinyint                             not null,
    deleted     tinyint                             not null,
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    updated_at  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint pay_method_id_user_id_uindex
        unique (id, user_id),
    constraint pay_method_uuid_uindex
        unique (uuid)
);


create table review
(
    id         bigint auto_increment
        primary key,
    uuid       varchar(36)                         not null,
    user_id    char(36)                            not null,
    item_id    char(36)                            not null,
    star       smallint                            not null,
    comment    varchar(1024)                       not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint review_id_user_id_item_id_uindex
        unique (id, user_id, item_id),
    constraint review_uuid_uindex
        unique (uuid)
);


create table role
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create table user
(
    id         bigint auto_increment
        primary key,
    uuid       varchar(36)                         not null,
    username   varchar(1024)                       not null,
    password   varchar(1024)                       not null,
    role_id    bigint                              not null,
    viewname   varchar(1024)                       not null,
    deleted    tinyint                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint user_uuid_uindex
        unique (uuid)
);

