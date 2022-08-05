package com.hlp.agrisys.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

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
    @Alias("Date")
    private LocalDate date;

    @ApiModelProperty(value = "the type of crop")
    @Alias("Type")
    private String crop;

    @ApiModelProperty(value = "supply of crops, in 10000 tons")
    @Alias("Supply")
    private Integer supply;

    @ApiModelProperty(value = "demand of crops, in 10000 tons")
    @Alias("Demand")
    private Integer demand;

    @ApiModelProperty(value = "unit price, GBP per ton")
    @Alias("Unit Price")
    private Integer unitPrice;


}
