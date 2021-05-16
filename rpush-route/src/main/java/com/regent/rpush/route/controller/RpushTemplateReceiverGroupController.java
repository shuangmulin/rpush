package com.regent.rpush.route.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.template.receiver.group.PageGroupParam;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.model.RpushTemplateReceiverGroup;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import com.regent.rpush.route.utils.PaginationUtil;
import com.regent.rpush.route.config.SessionUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 消息模板-接收人分组表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
@RestController
@RequestMapping("/rpush-template-receiver-group")
@PreAuthorize("hasAnyAuthority('admin')")
public class RpushTemplateReceiverGroupController {

    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @ApiOperation("接收人分组分页")
    @PostMapping("/{platform}")
    public ApiResult<Pagination<RpushTemplateReceiverGroup>> pageGroup(@PathVariable("platform") MessagePlatformEnum platform,
                                                                       @RequestBody @Valid @NotNull(message = "参数不全") PageGroupParam param) {
        int pageNum = PageUtil.getDefaultPageNum(param.getPageNum());
        int pageSize = PageUtil.getDefaultPageSize(param.getPageSize());
        Page<RpushTemplateReceiverGroup> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushTemplateReceiverGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("client_id", SessionUtils.getClientId());
        wrapper.eq("platform", platform.name());
        wrapper.like(StringUtils.isNotBlank(param.getGroupName()), "group_name", param.getGroupName());
        wrapper.eq(param.getId() != null, "id", param.getId());
        page = (Page<RpushTemplateReceiverGroup>) rpushTemplateReceiverGroupService.page(page, wrapper);
        Pagination<RpushTemplateReceiverGroup> pagination = PaginationUtil.convert(page);
        return ApiResult.of(pagination);
    }

    @ApiOperation("单个接收人分组查询")
    @GetMapping("/{platform}/{groupId}")
    public ApiResult<RpushTemplateReceiverGroup> pageGroup(@PathVariable("platform") MessagePlatformEnum platform,
                                                                       @PathVariable("groupId") Long groupId) {
        ApiResult<Pagination<RpushTemplateReceiverGroup>> pageResult = pageGroup(platform, PageGroupParam.builder().id(groupId).build());
        if (pageResult == null) {
            return ApiResult.of(null);
        }
        Pagination<RpushTemplateReceiverGroup> pagination = pageResult.getData();
        if (pagination == null) {
            return ApiResult.of(null);
        }
        List<RpushTemplateReceiverGroup> dataList = pagination.getDataList();
        if (dataList == null || dataList.size() <= 0) {
            return ApiResult.of(null);
        }
        return ApiResult.of(dataList.get(0));
    }

    @ApiOperation("新增或更新分组")
    @PostMapping
    public ApiResult<String> updateGroup(@RequestBody @Valid @NotNull(message = "参数不全") RpushTemplateReceiverGroup group) {
        rpushTemplateReceiverGroupService.updateGroup(group);
        return ApiResult.success();
    }

    @ApiOperation("删除分组")
    @DeleteMapping("/{id}")
    public ApiResult<String> deleteGroup(@PathVariable("id") Long id) {
        if (id == null) {
            return ApiResult.success();
        }
        rpushTemplateReceiverGroupService.delete(id);
        return ApiResult.success();
    }

}
