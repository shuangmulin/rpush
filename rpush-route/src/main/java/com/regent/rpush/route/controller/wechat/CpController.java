package com.regent.rpush.route.controller.wechat;

import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.route.dto.ReceiverBatchInsertDTO;
import com.regent.rpush.route.dto.wechat.CpUserImportParam;
import com.regent.rpush.route.model.RpushTemplateReceiverGroup;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import com.regent.rpush.route.service.IRpushTemplateReceiverService;
import com.regent.rpush.route.utils.RouteSingleton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业微信相关接口
 *
 * @author 钟宝林
 * @since 2021/4/19/019 23:00
 **/
@RestController
@RequestMapping("/wechat/cp")
@PreAuthorize("hasAnyAuthority('admin')")
public class CpController {

    @Autowired
    private IRpushTemplateReceiverService rpushTemplateReceiverService;
    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @GetMapping("/department")
    public ApiResult<List<WxCpDepart>> department(String corpId, String secret, Integer agentId, Long departmentId, boolean forceRefresh) throws WxErrorException {
        return ApiResult.of(RouteSingleton.wxCpDepartments(corpId, secret, agentId, departmentId, forceRefresh));
    }

    @GetMapping("/user")
    public ApiResult<List<WxCpUser>> user(String corpId, String secret, Integer agentId, Long departmentId) throws WxErrorException {
        WxCpServiceImpl wxCpService = RouteSingleton.wxCpService(corpId, secret, agentId);
        List<WxCpUser> users = new ArrayList<>();
        List<WxCpDepart> departs = wxCpService.getDepartmentService().list(departmentId);
        for (WxCpDepart depart : departs) {
            users.addAll(wxCpService.getUserService().listByDepartment(depart.getId(), true, 0));
        }
        return ApiResult.of(users);
    }

    @PostMapping("/import")
    public ApiResult<String> importUser(@RequestBody CpUserImportParam param) throws WxErrorException {
        WxCpServiceImpl wxCpService = RouteSingleton.wxCpService(param.getCorpId(), param.getSecret(), param.getAgentId());
        String corpId = param.getCorpId();
        String secret = param.getSecret();
        Integer agentId = param.getAgentId();
        Long groupId = param.getGroupId();
        RpushTemplateReceiverGroup group = rpushTemplateReceiverGroupService.getById(groupId);

        List<ReceiverBatchInsertDTO> receivers = new ArrayList<>();
        int importStyle = param.getImportStyle();
        if (importStyle == 0) {
            // 导入所有用户
            List<WxCpDepart> allDeparts = RouteSingleton.wxCpDepartments(corpId, secret, agentId, null, true);
            for (WxCpDepart depart : allDeparts) {
                List<WxCpUser> wxCpUsers = wxCpService.getUserService().listByDepartment(depart.getId(), true, 0);
                for (WxCpUser wxCpUser : wxCpUsers) {
                    receivers.add(ReceiverBatchInsertDTO.builder()
                            .receiverGroupName(group == null ? null : group.getGroupName())
                            .receiverId(wxCpUser.getUserId())
                            .receiverName(wxCpUser.getName())
                            .build());
                }
            }
        }

        if (importStyle == 1) {
            // 按部门导入
            List<Long> departmentIds = param.getDepartmentIds();
            List<WxCpDepart> departs;
            if (departmentIds.contains(-1L)) {
                // 全部部门
                departs = RouteSingleton.wxCpDepartments(corpId, secret, agentId, null, true);
            } else {
                // 部分部门
                departs = RouteSingleton.wxCpDepartmentsByIds(corpId, secret, agentId, departmentIds, true);
            }
            int departmentImportStyle = param.getDepartmentImportStyle();
            for (WxCpDepart depart : departs) {
                List<WxCpUser> wxCpUsers = wxCpService.getUserService().listByDepartment(depart.getId(), true, 0);
                for (WxCpUser wxCpUser : wxCpUsers) {
                    String groupName = departmentImportStyle == 0 ? depart.getName() : (group == null ? null : group.getGroupName()); // 按部门导入方式，0 按部门名称创建 1 导入指定分组
                    receivers.add(ReceiverBatchInsertDTO.builder()
                            .receiverGroupName(groupName)
                            .receiverId(wxCpUser.getUserId())
                            .receiverName(wxCpUser.getName())
                            .build());
                }
            }
        }
        rpushTemplateReceiverService.batchInsert(MessagePlatformEnum.WECHAT_WORK_AGENT, receivers);
        return ApiResult.of("导入成功");
    }

}
