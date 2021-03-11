package com.regent.rpush.route.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.template.receiver.PageReceiverParam;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.model.RpushTemplateReceiver;
import com.regent.rpush.route.model.RpushTemplateReceiverGroup;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import com.regent.rpush.route.service.IRpushTemplateReceiverService;
import com.regent.rpush.route.utils.PaginationUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息模板-预设接收人表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
 */
@RestController
@RequestMapping("/rpush-template-receiver")
public class RpushTemplateReceiverController {
    @Autowired
    private IRpushTemplateReceiverService rpushTemplateReceiverService;
    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @ApiOperation("接收人分页")
    @PostMapping("/{platform}")
    public ApiResult<Pagination<RpushTemplateReceiver>> page(@PathVariable("platform") MessagePlatformEnum platform,
                                                             @RequestBody @Valid @NotNull(message = "参数不全") PageReceiverParam param) {
        int pageNum = PageUtil.getDefaultPageNum(param.getPageNum());
        int pageSize = PageUtil.getDefaultPageSize(param.getPageSize());
        Page<RpushTemplateReceiver> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushTemplateReceiver> wrapper = new QueryWrapper<>();
        wrapper.eq("platform", platform.name());
        wrapper.eq(StringUtils.isNotBlank(param.getReceiverName()), "receiver_name", param.getReceiverName());
        wrapper.eq(param.getGroupId() != null, "group_id", param.getGroupId());
        wrapper.like(StringUtils.isNotBlank(param.getReceiverId()), "receiver_id", param.getReceiverId());
        wrapper.eq(param.getId() != null, "id", param.getId());
        page = (Page<RpushTemplateReceiver>) rpushTemplateReceiverService.page(page, wrapper);
        Pagination<RpushTemplateReceiver> pagination = PaginationUtil.convert(page);

        List<RpushTemplateReceiver> dataList = pagination.getDataList();
        if (dataList != null && dataList.size() > 0) {
            // 补一下关联的分组名称
            List<Long> groupIds = dataList.stream().map(RpushTemplateReceiver::getGroupId).collect(Collectors.toList());
            groupIds.removeIf(Objects::isNull);
            if (groupIds.size() > 0) {
                Collection<RpushTemplateReceiverGroup> groups = rpushTemplateReceiverGroupService.listByIds(groupIds);
                Map<Long, String> groupNameMap = groups.stream().collect(Collectors.toMap(RpushTemplateReceiverGroup::getId, RpushTemplateReceiverGroup::getGroupName));
                for (RpushTemplateReceiver templateReceiver : dataList) {
                    Long receiverGroupId = templateReceiver.getGroupId();
                    if (receiverGroupId == null) {
                        continue;
                    }
                    templateReceiver.setGroupName(groupNameMap.get(templateReceiver.getGroupId()));
                }
            }
        }

        return ApiResult.of(pagination);
    }

    @ApiOperation("单个接收人查询")
    @GetMapping("/{platform}/{id}")
    public ApiResult<RpushTemplateReceiver> page(@PathVariable("platform") MessagePlatformEnum platform,
                                                 @PathVariable("id") Long id) {
        ApiResult<Pagination<RpushTemplateReceiver>> pageResult = page(platform, PageReceiverParam.builder().id(id).build());
        if (pageResult == null) {
            return ApiResult.of(null);
        }
        Pagination<RpushTemplateReceiver> pagination = pageResult.getData();
        if (pagination == null) {
            return ApiResult.of(null);
        }
        List<RpushTemplateReceiver> dataList = pagination.getDataList();
        if (dataList == null || dataList.size() <= 0) {
            return ApiResult.of(null);
        }
        return ApiResult.of(dataList.get(0));
    }

    @ApiOperation("新增或更新接收人")
    @PostMapping
    public ApiResult<String> updateReceiver(@RequestBody @Valid @NotNull(message = "参数不全") RpushTemplateReceiver receiver) {
        rpushTemplateReceiverService.updateReceiver(receiver);
        return ApiResult.success();
    }

    @ApiOperation("删除接收人")
    @DeleteMapping("/{id}")
    public ApiResult<String> delete(@PathVariable("id") Long id) {
        if (id == null) {
            return ApiResult.success();
        }
        rpushTemplateReceiverService.removeById(id);
        return ApiResult.success();
    }
}
