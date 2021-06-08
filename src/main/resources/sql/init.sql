create table scene
(
  id            int auto_increment
    primary key,
  scene_name    varchar(40)   not null,
  scene_desc    varchar(200)  null,
  scene_type    int default 1 not null comment '0-手动 1-自动',
  auto_switch   int default 1 null comment '0-关 1-开',
  scene_icon    varchar(100)  null,
  sort          int default 1 not null,
  trigger_all   int default 0 null comment '1-同时满足才触发',
  condition_all int default 0 null comment '1-同时满足才执行',
  user_uuid     varchar(40)   not null,
  create_time   datetime      not null,
  update_time   datetime      null
);

create table scene_attribute
(
  id              int auto_increment
    primary key,
  product_key     varchar(40)   not null,
  sort            int default 1 not null,
  type            int           not null comment '类别：1-if（trigger/condition），2-then（action）',
  auto_type       int default 0 not null comment 'if节点下的自动化类型：0-瞬时类，1-状态类',
  auto_name       varchar(40)   not null,
  data_type       int           not null comment '数据类型：1：布尔，2：整数，3：浮点，4：字符串，5：枚举',
  attribute_id    int           not null,
  attribute_key   varchar(40)   not null,
  attribute_name  varchar(40)   null,
  attribute_value varchar(1000) null,
  state           int default 1 null comment '自动化属性状态，0-删除，1-使用，2-停用',
  create_time     datetime      null,
  update_time     datetime      null
);

create table scene_action
(
  id                 int auto_increment
    primary key,
  scene_id           int           not null,
  user_uuid          varchar(40)   not null,
  product_key        varchar(40)   null,
  device_uuid        varchar(40)   null,
  action_type        int           not null comment '1-延时 2-设备属性 3-通知',
  sort               int default 1 not null,
  create_time        datetime      null,
  update_time        datetime      null,
  delay_second       int           null,
  content            varchar(200)  null,
  scene_attribute_id int           null,
  attribute_key      varchar(40)   null,
  data_type          int           null,
  expect_value       varchar(40)   null
);

create table scene_condition
(
  id             int auto_increment
    primary key,
  scene_id       int           not null,
  node_type      int           null comment '节点类型：1-trigger 2-condition',
  user_uuid      varchar(40)   not null,
  sort           int default 1 not null,
  condition_type int           not null comment '1-时间点 2-时间段 3-设备属性',
  condition_json varchar(1000) null,
  create_time    datetime      null,
  update_time    datetime      null
);
