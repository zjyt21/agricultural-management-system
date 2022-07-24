package com.hlp.agrisys.entity;

import java.io.Serializable;
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
 * @since 2022-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Dict Object", description="")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "value")
    private String value;

    @ApiModelProperty(value = "type")
    private String type;


}
