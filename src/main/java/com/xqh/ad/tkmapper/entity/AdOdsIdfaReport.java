package com.xqh.ad.tkmapper.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "`ad_ods_idfa_report`")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdOdsIdfaReport {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 推广应用-媒体主键
     */
    @Column(name = "app_media_id")
    private String appMediaId;

    /**
     * ip
     */
    private String ip;

    /**
     * 手机标识idfa
     */
    private String idfa;

    /**
     * 状态 0-初始 1-已发送消息队列
     */
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;
}