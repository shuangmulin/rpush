package com.regent.rpush.route.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.his.PageHisDetailParam;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.model.RpushMessageHis;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushMessageHisDetailService;
import com.regent.rpush.route.service.IRpushMessageHisService;
import com.regent.rpush.route.utils.PaginationUtil;
import com.regent.rpush.route.utils.Qw;
import com.regent.rpush.route.config.SessionUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息历史记录详情表 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-16
 */
@RestController
@RequestMapping("/rpush-message-his-detail")
@PreAuthorize("hasAnyAuthority('admin')")
public class RpushMessageHisDetailController {

    @Autowired
    private IRpushMessageHisDetailService rpushMessageHisDetailService;
    @Autowired
    private IRpushMessageHisService rpushMessageHisService;

    @ApiOperation("历史消息分页")
    @PostMapping
    public ApiResult<Pagination<RpushMessageHisDetail>> page(@RequestBody @Valid @NotNull(message = "参数不全") PageHisDetailParam param) {
        String clientId = SessionUtils.getClientId();
        int pageNum = PageUtil.getDefaultPageNum(param.getPageNum());
        int pageSize = PageUtil.getDefaultPageSize(param.getPageSize());
        Page<RpushMessageHisDetail> page = new Page<>(pageNum, pageSize);
        QueryWrapper<RpushMessageHisDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("client_id", clientId);
        wrapper.in(param.getPlatform() != null && param.getPlatform().size() > 0, "platform", param.getPlatform());
        wrapper.like(StringUtils.isNotBlank(param.getRequestNo()), "request_no", param.getRequestNo());
        wrapper.in(param.getSendStatus() != null && param.getSendStatus().size() > 0, "send_status", param.getSendStatus());
        wrapper.like(StringUtils.isNotBlank(param.getReceiverId()), "receiver_id", param.getReceiverId());
        wrapper.between(param.getDateCreatedEnd() != null && param.getDateCreatedStart() != null, "date_created", param.getDateCreatedStart(), param.getDateCreatedEnd());
        wrapper.orderBy(true, false, "date_created");
        page = (Page<RpushMessageHisDetail>) rpushMessageHisDetailService.page(page, wrapper);
        Pagination<RpushMessageHisDetail> pagination = PaginationUtil.convert(page);

        List<RpushMessageHisDetail> dataList = pagination.getDataList();
        if (dataList != null && dataList.size() > 0) {
            List<String> requestNos = dataList.stream().map(RpushMessageHisDetail::getRequestNo).collect(Collectors.toList());
            List<RpushMessageHis> rpushMessageHisList = rpushMessageHisService.list(Qw.newInstance(RpushMessageHis.class).in("request_no", requestNos));
            Map<String, String> paramJsonMap = rpushMessageHisList.stream().collect(Collectors.toMap(RpushMessageHis::getRequestNo, RpushMessageHis::getParam));

            // 补一下各个字段的名称
            for (RpushMessageHisDetail detail : dataList) {
                detail.setPlatformAlias(MessagePlatformEnum.valueOf(detail.getPlatform()).getName()); // 平台名称
                detail.setSendStatusName(RpushMessageHisDetail.SEND_STATUS_NAME_MAP.get(detail.getSendStatus())); // 状态名称
                detail.setParamJson(paramJsonMap.get(detail.getRequestNo())); // 请求参数
            }
        }

        return ApiResult.of(pagination);
    }

}
