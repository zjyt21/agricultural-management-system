package com.hlp.agrisys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hlp
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AgriActivity Object", description="")
public class AgriActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT")
    private LocalDate date;

    @ApiModelProperty(value = "specific activity")
    private String type;

    @ApiModelProperty(value = "quantity")
    private Integer quantity;

    @ApiModelProperty(value = "the user of recording")
    private Long operator;

    @ApiModelProperty(value = "the type of crop")
    private String crop;

}
