-- 文档内容
-- doc.id == content.id ；content是大字段，要做分表操作
drop table if exists `content`;
create table `content` (
  `id` bigint not null comment '文档id',
  `content` mediumtext not null comment '内容',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档内容';