package com.hlp.agrisys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * menu table
 * </p>
 *
 * @author hlp
 * @since 2022-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Menu对象", description="menu table")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "menu name")
    private String name;

    @ApiModelProperty(value = "route address")
    private String path;

    @ApiModelProperty(value = "component path")
    private String pagePath;

    @ApiModelProperty(value = "Menu status (0 normal 1 disabled)")
    private String status;

    @ApiModelProperty(value = "Permissions")
    private String perms;

    @ApiModelProperty(value = "menu icon")
    private String icon;

    @ApiModelProperty(value = "Whether to delete (0 not deleted 1 deleted)")
    private Integer deleted;

    @ApiModelProperty(value = "Description")
    private String description;

    @ApiModelProperty(value = "children menu")
    @TableField(exist = false)
    private List<Menu> children;

    @ApiModelProperty(value = "parent id")
    private Long pid;

}
