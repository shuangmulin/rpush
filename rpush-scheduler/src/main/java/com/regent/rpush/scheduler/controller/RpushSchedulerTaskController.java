package com.regent.rpush.scheduler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.scheduler.dto.PageTaskParam;
import com.regent.rpush.scheduler.model.RpushSchedulerTask;
import com.regent.rpush.scheduler.service.IRpushSchedulerTaskService;
import com.regent.rpush.scheduler.utils.PaginationUtil;
import com.regent.rpush.scheduler.auth.SessionUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 调度任务表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-05-02
 */
@RestController
@RequestMapping("/rpush-scheduler-task")
@CrossOrigin
@PreAuthorize("hasAnyAuthority('admin')")
public class RpushSchedulerTaskController {

    @Autowired
    private IRpushSchedulerTaskService rpushSchedulerTaskService;

    @PostMapping("/page")
    public ApiResult<Pagination<RpushSchedulerTask>> page(@RequestBody PageTaskParam param) {
        int pageNum = PageUtil.getDefaultPageNum(param.getPageNum());
        int pageSize = PageUtil.getDefaultPageSize(param.getPageSize());
        Page<RpushSchedulerTask> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushSchedulerTask> wrapper = new QueryWrapper<>();
        wrapper.eq(param.getId() != null, "id", param.getId());
        wrapper.eq( "client_id", SessionUtils.getClientId());
        wrapper.eq(param.getEnableFlag() != null, "enable_flag", param.getEnableFlag());
        wrapper.like(StringUtils.isNotBlank(param.getJobName()), "job_name", param.getJobName());
        wrapper.like(StringUtils.isNotBlank(param.getJobGroup()), "job_group", param.getJobGroup());
        page = (Page<RpushSchedulerTask>) rpushSchedulerTaskService.page(page, wrapper);
        Pagination<RpushSchedulerTask> pagination = PaginationUtil.convert(page);
        return ApiResult.of(pagination);
    }

    @ApiOperation("单个任务查询")
    @GetMapping("/detail/{taskId}")
    public ApiResult<RpushSchedulerTask> task(@PathVariable("taskId") Long taskId) {
        ApiResult<Pagination<RpushSchedulerTask>> pageResult = page(PageTaskParam.builder().id(taskId).build());
        if (pageResult == null) {
            return ApiResult.of(null);
        }
        Pagination<RpushSchedulerTask> pagination = pageResult.getData();
        if (pagination == null) {
            return ApiResult.of(null);
        }
        List<RpushSchedulerTask> dataList = pagination.getDataList();
        if (dataList == null || dataList.size() <= 0) {
            return ApiResult.of(null);
        }
        return ApiResult.of(dataList.get(0));
    }

    @ApiOperation("新增任务")
    @PostMapping("/add")
    public ApiResult<String> addTask(@RequestBody @Valid @NotNull(message = "参数不全") RpushSchedulerTask task) {
        rpushSchedulerTaskService.addJob(task);
        return ApiResult.success();
    }

    @ApiOperation("更新任务")
    @PutMapping("/update")
    public ApiResult<String> updateTask(@RequestBody @Valid @NotNull(message = "参数不全") RpushSchedulerTask task) throws SchedulerException {
        rpushSchedulerTaskService.updateTask(task);
        return ApiResult.success();
    }

    @ApiOperation("删除任务")
    @DeleteMapping("/delete/{id}")
    public ApiResult<String> deleteGroup(@PathVariable("id") Long id) throws SchedulerException {
        if (id == null) {
            return ApiResult.success();
        }
        rpushSchedulerTaskService.delete(id);
        rpushSchedulerTaskService.disableTask(id);
        return ApiResult.success();
    }

    @ApiOperation("启用或停用一个任务")
    @PostMapping("/disable-enable/{id}")
    public ApiResult<String> disableOrEnable(@PathVariable("id") Long id, boolean disableOrEnable) throws SchedulerException {
        if (id == null) {
            return ApiResult.success();
        }
        rpushSchedulerTaskService.disableOrEnable(id, disableOrEnable);
        return ApiResult.success();
    }

}
