-- 文档表
drop table if exists `doc`;
create table `doc` (
  `id` bigint not null comment 'id',
  `ebook_id` bigint not null default 0 comment '电子书id',
  `parent` bigint not null default 0 comment '父id',
  `name` varchar(50) not null comment '名称',
  `sort` int comment '顺序',
  `view_count` int default 0 comment '阅读数',
  `vote_count` int default 0 comment '点赞数',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档';

insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, '文档1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, '文档1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, '文档2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, '文档2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, '文档2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, '文档2.2.1', 1, 0, 0);

# 统计某一个电子书数据
select count(1),sum(view_count),sum(vote_count) from doc where ebook_id=1;

# 全部统计
select ebook_id,count(1) doc_count,sum(view_count) view_count,sum(vote_count) vote_count from doc
group by ebook_id;

# 联表更新电子书表格数据
update ebook t1,(select ebook_id,count(1) doc_count,sum(view_count) view_count,sum(vote_count) vote_count from doc
                 group by ebook_id) t2
set t1.doc_count=t2.doc_count,
    t1.vote_count=t2.vote_count,
    t1.view_count=t2.view_count
where t1.id=t2.ebook_id
