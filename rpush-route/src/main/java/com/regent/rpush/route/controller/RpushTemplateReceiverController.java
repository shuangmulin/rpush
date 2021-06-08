package com.regent.rpush.route.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rpush.common.PageUtil;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.route.template.receiver.PageReceiverParam;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.route.dto.ReceiverBatchInsertDTO;
import com.regent.rpush.route.model.RpushTemplateReceiver;
import com.regent.rpush.route.model.RpushTemplateReceiverGroup;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import com.regent.rpush.route.service.IRpushTemplateReceiverService;
import com.regent.rpush.route.utils.PaginationUtil;
import com.regent.rpush.route.config.SessionUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
@PreAuthorize("hasAnyAuthority('admin')")
public class RpushTemplateReceiverController {
    private final static Logger LOGGER = LoggerFactory.getLogger(RpushTemplateReceiverController.class);

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
        wrapper.eq("client_id", SessionUtils.getClientId());
        wrapper.eq("platform", platform.name());
        wrapper.like(StringUtils.isNotBlank(param.getReceiverName()), "receiver_name", param.getReceiverName());
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
        rpushTemplateReceiverService.delete(id);
        return ApiResult.success();
    }


    @GetMapping("download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 HttpServletRequest request) {
        Resource resource = new ClassPathResource("/file" + File.separator + fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("无法获取文件类型", e);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @ApiOperation("Excel导入")
    @PostMapping("/import")
    @ResponseBody
    @CrossOrigin
    public ApiResult<String> upload(@RequestParam("platform") MessagePlatformEnum platform, @RequestParam("file") MultipartFile file) throws IOException {
        if (platform == null) {
            return ApiResult.of("未知平台");
        }
        if (file.isEmpty()) {
            return ApiResult.of("上传文件不能为空");
        }
        EasyExcel.read(file.getInputStream(), ReceiverBatchInsertDTO.class, new ImportReceiverListener(platform, rpushTemplateReceiverService)).headRowNumber(3).sheet().doRead(); // 导入
        return ApiResult.success();
    }

    private static class ImportReceiverListener extends AnalysisEventListener<ReceiverBatchInsertDTO> {
        /**
         * 每隔3000条存储数据库
         */
        private static final int BATCH_COUNT = 3000;
        private final List<ReceiverBatchInsertDTO> list = new ArrayList<>();

        private final IRpushTemplateReceiverService rpushTemplateReceiverService;
        private final MessagePlatformEnum platform;

        ImportReceiverListener (MessagePlatformEnum platform, IRpushTemplateReceiverService rpushTemplateReceiverService) {
            this.platform = platform;
            this.rpushTemplateReceiverService = rpushTemplateReceiverService;
        }

        @Override
        public void invoke(ReceiverBatchInsertDTO data, AnalysisContext context) {
            list.add(data);
            if (list.size() >= BATCH_COUNT) {
                saveData();
                list.clear();
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            saveData();
        }

        /**
         * 加上存储数据库
         */
        private void saveData() {
            rpushTemplateReceiverService.batchInsert(platform, list);
        }
    }
}
