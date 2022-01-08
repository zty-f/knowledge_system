-- 电子书快照表
drop table if exists `ebook_snapshot`;
create table `ebook_snapshot`
(
    `id`            bigint auto_increment not null comment 'id',
    `ebook_id`      bigint                not null default 0 comment '电子书id',
    `date`          date                  not null comment '快照日期',
    `view_count`    int                   not null default 0 comment '阅读数',
    `vote_count`    int                   not null default 0 comment '点赞数',
    `view_increase` int                   not null default 0 comment '阅读增长',
    `vote_increase` int                   not null default 0 comment '点赞增长',
    primary key (`id`),
    unique key `ebook_id_date_unique` (`ebook_id` , `date`)
) engine = innodb
  default charset = utf8mb4 comment ='电子书快照表';

# 增加唯一键 unique key `ebook_id_date_unique` (`ebook_id` , `date`)

# 方案一（ID不连续）：
#   删除今天的数据
#   为所有的电子书生成一条今天的记录
#   更新总阅读数、总点赞数
#   更新今日阅读数、今日点赞数
# 方案二（ID连续）：
#   为所有的电子书生成一条今天的记录，如果还没有
#   更新总阅读数、总点赞数
#   更新今日阅读数、今日点赞数
insert into ebook_snapshot(ebook_id, `date`, view_count, vote_count, view_increase, vote_increase)
select t1.id, curdate(), 0, 0, 0, 0
from ebook t1
where not exists(select 1
                 from ebook_snapshot t2
                 where t1.id = t2.ebook_id
                   and t2.`date` = curdate());

update ebook_snapshot t1, ebook t2
set t1.view_count = t2.view_count,
    t1.vote_count = t2.vote_count
where t1.`date` = curdate()
  and t1.ebook_id = t2.id;

update ebook_snapshot t1 left join (select ebook_id, view_count, vote_count
                                    from ebook_snapshot
                                    where `date` = date_sub(curdate(), interval 1 day)) t2
    on t1.ebook_id = t2.ebook_id
set t1.view_increase = (t1.view_count - ifnull(t2.view_count, 0)),
    t1.vote_increase = (t1.vote_count - ifnull(t2.vote_count, 0))
where t1.`date` = curdate();
