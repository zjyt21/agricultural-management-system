package com.hlp.agrisys.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Format of response data
 *
 * @author Mr.Han
 * @create 2022-07-07 20:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    /**
     * status code
     */
    private Integer code;
    /**
     * Prompt information, if there is an error, the front end can get this field to prompt
     */
    private String msg;
    /**
     * Query result data,
     */
    private T data;

    public static Result success(){
        return new Result(200, "操作成功");
    }

    public static Result success(Object data){
        return new Result(200, data);
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

}