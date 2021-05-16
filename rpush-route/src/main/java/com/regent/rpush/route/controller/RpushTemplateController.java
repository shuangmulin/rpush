package com.regent.rpush.route.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.template.PageTemplateParam;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.model.RpushTemplate;
import com.regent.rpush.route.model.RpushTemplateReceiverGroup;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import com.regent.rpush.route.service.IRpushTemplateService;
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
 * 消息模板表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-04
 */
//@RestController
//@RequestMapping("/rpush-template")
//@PreAuthorize("hasAnyAuthority('admin')")
public class RpushTemplateController {

    @Autowired
    private IRpushTemplateService rpushTemplateService;

    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @ApiOperation("模板分页")
    @PostMapping("/{platform}")
    public ApiResult<Pagination<RpushTemplate>> pageTemplate(@PathVariable("platform") MessagePlatformEnum platform,
                                                             @RequestBody @Valid @NotNull(message = "参数不全") PageTemplateParam param) {
        int pageNum = PageUtil.getDefaultPageNum(param.getPageNum());
        int pageSize = PageUtil.getDefaultPageSize(param.getPageSize());
        Page<RpushTemplate> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("platform", platform.name());
        wrapper.eq(param.getReceiverGroupId() != null && param.getReceiverGroupId() > 0, "receiver_group_id", param.getReceiverGroupId());
        wrapper.like(StringUtils.isNotBlank(param.getTemplateName()), "template_name", param.getTemplateName());
        wrapper.eq(param.getId() != null, "id", param.getId());
        page = (Page<RpushTemplate>) rpushTemplateService.page(page, wrapper);
        Pagination<RpushTemplate> pagination = PaginationUtil.convert(page);
        List<RpushTemplate> dataList = pagination.getDataList();
        if (dataList != null && dataList.size() > 0) {
            // 补一下关联的分组名称
            List<Long> groupIds = dataList.stream().map(RpushTemplate::getReceiverGroupId).collect(Collectors.toList());
            groupIds.removeIf(Objects::isNull);
            if (groupIds.size() > 0) {
                Collection<RpushTemplateReceiverGroup> groups = rpushTemplateReceiverGroupService.listByIds(groupIds);
                Map<Long, String> groupNameMap = groups.stream().collect(Collectors.toMap(RpushTemplateReceiverGroup::getId, RpushTemplateReceiverGroup::getGroupName));
                for (RpushTemplate rpushTemplate : dataList) {
                    Long receiverGroupId = rpushTemplate.getReceiverGroupId();
                    if (receiverGroupId == null) {
                        continue;
                    }
                    rpushTemplate.setReceiverGroupName(groupNameMap.get(rpushTemplate.getReceiverGroupId()));
                }
            }
        }
        return ApiResult.of(pagination);
    }

    @ApiOperation("单个模板查询")
    @GetMapping("/{platform}/{templateId}")
    public ApiResult<RpushTemplate> pageTemplate(@PathVariable("platform") MessagePlatformEnum platform,
                                                 @PathVariable("templateId") Long templateId) {
        ApiResult<Pagination<RpushTemplate>> pageResult = pageTemplate(platform, PageTemplateParam.builder().id(templateId).build());
        if (pageResult == null) {
            return ApiResult.of(null);
        }
        Pagination<RpushTemplate> pagination = pageResult.getData();
        if (pagination == null) {
            return ApiResult.of(null);
        }
        List<RpushTemplate> dataList = pagination.getDataList();
        if (dataList == null || dataList.size() <= 0) {
            return ApiResult.of(null);
        }
        return ApiResult.of(dataList.get(0));
    }

    @ApiOperation("新增或更新模板")
    @PostMapping
    public ApiResult<String> updateGroup(@RequestBody @Valid @NotNull(message = "参数不全") RpushTemplate rpushTemplate) {
        rpushTemplateService.updateTemplate(rpushTemplate);
        return ApiResult.success();
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/{id}")
    public ApiResult<String> deleteGroup(@PathVariable("id") Long id) {
        if (id == null) {
            return ApiResult.success();
        }
        rpushTemplateService.removeById(id);
        return ApiResult.success();
    }

}
