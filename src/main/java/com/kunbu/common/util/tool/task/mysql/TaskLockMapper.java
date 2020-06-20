package com.kunbu.common.util.tool.task.mysql;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @author: KunBu
 * @time: 2020/6/20 15:48
 * @description:
 */
@Component
@Mapper
public interface TaskLockMapper {

    String SQL = "`id`,`code`,`name`,`status`,`createTime`,`version`";

    @Insert({
            "insert into `task_lock` (",
            SQL,
            ") values (",
            "#{dto.code},#{dto.name},#{dto.status},#{dto.createTime},#{dto.version}"
    })
    int insert(@Param("dto") TaskLock dto);

    @Delete({"delete from `task_lock` where code=#{code}"})
    int delete(@Param("code")String code);

    @Update({
            "update `task_lock` SET ",
            "`code` = #{dto.code},",
            "`name` = #{dto.name}",
            "where `id` = #{dto.id}"
    })
    int update(@Param("dto") TaskLock dto);

    @Select({
            "<script>",
            "select " + SQL + " from `task_lock` ",
            "order by createTime desc ",
            "</script>"
    })
    Page<TaskLock> page();

    @Select({"select count(*) from `task_lock` where code=#{code} "})
    int checkByCode(@Param("code")String code);

    @Select({"select " + SQL + " from `task_lock` where id=#{id} LIMIT 1"})
    TaskLock findById(@Param("id")Integer id);

    @Select({"select " + SQL + " from `task_lock` where code=#{code} LIMIT 1"})
    TaskLock findByCode(@Param("code")String code);

    @Update({
            "update task_lock ",
            "set status=1, version=version + 1 ",
            "where code=#{code} and version =#{version} and status=0 "
    })
    int requireLock(@Param("code")String code, @Param("version")Integer version);

    @Update({
            "update task_lock ",
            "set status=0 ",
            "where code=#{code} and status=1 "
    })
    int releaseLock(@Param("code")String code);
}
