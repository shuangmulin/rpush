package com.regent.rpush.route.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.template.receiver.PageReceiverParam;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.model.RpushTemplateReceiver;
import com.regent.rpush.route.service.IRpushTemplateReceiverService;
import com.regent.rpush.route.utils.PaginationUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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
        wrapper.eq(param.getGroupId() != null, "groupId", param.getGroupId());
        wrapper.like(StringUtils.isNotBlank(param.getReceiverId()), "receiver_id", param.getReceiverId());
        wrapper.eq(param.getId() != null, "id", param.getId());
        page = (Page<RpushTemplateReceiver>) rpushTemplateReceiverService.page(page, wrapper);
        Pagination<RpushTemplateReceiver> pagination = PaginationUtil.convert(page);
        return ApiResult.of(pagination);
    }

    @ApiOperation("单个接收人查询")
    @GetMapping("/{platform}/{Id}")
    public ApiResult<RpushTemplateReceiver> page(@PathVariable("platform") MessagePlatformEnum platform,
                                                           @PathVariable("Id") Long Id) {
        ApiResult<Pagination<RpushTemplateReceiver>> pageResult = page(platform, PageReceiverParam.builder().id(Id).build());
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

    @ApiOperation("新增或更新分组")
    @PostMapping
    public ApiResult<String> updateReceiver(@RequestBody @Valid @NotNull(message = "参数不全") RpushTemplateReceiver receiver) {
        rpushTemplateReceiverService.updateReceiver(receiver);
        return ApiResult.success();
    }

    @ApiOperation("删除分组")
    @DeleteMapping("/{id}")
    public ApiResult<String> delete(@PathVariable("id") Long id) {
        if (id == null) {
            return ApiResult.success();
        }
        rpushTemplateReceiverService.removeById(id);
        return ApiResult.success();
    }
}
