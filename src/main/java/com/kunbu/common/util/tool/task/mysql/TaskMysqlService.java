package com.kunbu.common.util.tool.task.mysql;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kunbu.common.util.web.PageResult;
import com.kunbu.common.util.web.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-06-20 15:45
 **/
@Component
public class TaskMysqlService {

    @Autowired
    private TaskLockMapper taskLockMapper;

    public ServiceResult<TaskLock> save(TaskLock dto) {
        if (dto.getId() == null) {
            if (taskLockMapper.checkByCode(dto.getCode()) > 0) {
                return ServiceResult.fail("定时任务code已存在");
            }
            TaskLock insert = new TaskLock();
            insert.setCode(dto.getCode());
            insert.setName(dto.getName());
            insert.setCreateTime(new Date());
            insert.setStatus(TaskLock.STATUS_FREE);
            insert.setVersion(0);
            taskLockMapper.insert(insert);
        } else {
            TaskLock old = taskLockMapper.findById(dto.getId());
            if (old == null) {
                return ServiceResult.fail("定时任务不存在");
            }
            if (!old.getCode().equals(dto.getCode())) {
                if (taskLockMapper.checkByCode(dto.getCode()) > 0) {
                    return ServiceResult.fail("定时任务code已存在");
                }
            }
            old.setCode(dto.getCode());
            old.setName(dto.getName());
            taskLockMapper.update(old);
        }
        return ServiceResult.success();
    }

    public ServiceResult<TaskLock> detail(Integer id) {
        TaskLock old = taskLockMapper.findById(id);
        if (old == null) {
            return ServiceResult.fail("定时任务不存在");
        } else {
            return ServiceResult.success(old);
        }
    }

    public ServiceResult<PageResult<TaskLock>> page(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TaskLock> page = taskLockMapper.page();

        PageResult result = PageResult.init(pageNum, pageSize);
        result.setList(page);
        result.setTotal(page.getTotal());
        // 7/10=1， 20/10=2， 13/10=2
        result.setPages((long) Math.ceil(page.getTotal() / pageSize));
        return ServiceResult.success(result);
    }

    public TaskLock findByCode(String code) {
        return taskLockMapper.findByCode(code);
    }

    public boolean requireLock(TaskLock lock) {
        return taskLockMapper.requireLock(lock.getCode(), lock.getVersion()) > 0;
    }

    public boolean releaseLock(TaskLock lock) {
        return taskLockMapper.releaseLock(lock.getCode()) > 0;
    }
}
