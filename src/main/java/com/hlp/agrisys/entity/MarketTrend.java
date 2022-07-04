package com.hlp.agrisys.entity;

import java.time.LocalDate;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *  Market class, which is a class that records the market supply and demand relationship and the unit price of crops
 * </p>
 *
 * @author hlp
 * @since 2022-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MarketTrend object", description="")
public class MarketTrend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "the record's date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT")
    private LocalDate date;

    @ApiModelProperty(value = "supply of crops, in 10000 tons")
    private Integer supply;

    @ApiModelProperty(value = "demand of crops, in 10000 tons")
    private Integer demand;

    @ApiModelProperty(value = "the type of crop")
    private String crop;

    @ApiModelProperty(value = "unit price, GBP per ton")
    private Integer unitPrice;


}
