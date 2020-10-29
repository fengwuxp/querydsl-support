package com.wuxp.querydsl.entities;

import com.wuxp.querydsl.enums.GoodsApprovalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author wxup
 */
@Table(name = "t_goods_audit_record_log",indexes = {
        @Index(name = "index_goods_audit_record_log_goods_id",columnList = "goods_id")
})
@Entity
@Schema(description = "商品审核记录")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsAuditRecordLog implements Serializable {

    private static final long serialVersionUID = -763549764304057103L;

    @Schema(description = "Id")
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Schema(description = "商品id")
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Schema(description = "审核状态")
    @Column(name = "approval_status", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private GoodsApprovalStatus approvalStatus;

    @Schema(description = "审核说明")
    @Column(name = "description", nullable = false)
    private String description;

    @Schema(description = "操作人")
    @Column(name = "operator", length = 16)
    private String operator;

    @Schema(description = "操作人名称")
    @Column(name = "operator_name", length = 16)
    private String operatorName;

    @Schema(description = "创建时间")
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime;

    @Schema(description = "更新时间")
    @Column(name = "last_update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdateTime;
}
