package com.hlp.agrisys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Soil Condition Object", description="")
public class Soil implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "date of this record")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT")
    private LocalDate date;

    @ApiModelProperty(value = "The force required to penetrate ten centimeters of soil, unit: N")
    private Double compaction;

    @ApiModelProperty(value = "soil temperature, unit: °C")
    private Double soilTemperature;

    @ApiModelProperty(value = "pH")
    @TableField("pH")
    private Double ph;

    @ApiModelProperty(value = "water content")
    private Double water;

    @ApiModelProperty(value = "nitrogen content")
    private Double nitrogen;

    @ApiModelProperty(value = "phosphorus content")
    private Double phosphorus;

    @ApiModelProperty(value = "soil condition")
    private Double score;
}
