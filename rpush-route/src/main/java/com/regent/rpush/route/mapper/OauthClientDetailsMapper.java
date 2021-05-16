package com.regent.rpush.route.mapper;

import com.regent.rpush.route.model.OauthClientDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 钟宝林
 * @since 2021-05-16
 */
public interface OauthClientDetailsMapper extends BaseMapper<OauthClientDetails> {

    @Update("REPLACE INTO `oauth_client_details` ( " +
            " `client_id`, " +
            " `resource_ids`, " +
            " `client_secret`, " +
            " `scope`, " +
            " `authorized_grant_types`, " +
            " `web_server_redirect_uri`, " +
            " `authorities`, " +
            " `access_token_validity`, " +
            " `refresh_token_validity`, " +
            " `additional_information`, " +
            " `autoapprove` " +
            ") " +
            "VALUES " +
            " ( " +
            "  #{superAdminUsername}, " +
            "  NULL, " +
            "  #{superAdminPass}, " +
            "  'all', " +
            "  'refresh_token,client_credentials', " +
            "  NULL, " +
            "  'super-admin', " +
            "  '3600', " +
            "  NULL, " +
            "  NULL, " +
            "  NULL " +
            " );")
    void initSuperAdmin(@Param("superAdminUsername") String superAdminUsername, @Param("superAdminPass") String superAdminPass);
}
