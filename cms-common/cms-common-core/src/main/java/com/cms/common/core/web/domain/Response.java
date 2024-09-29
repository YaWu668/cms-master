package com.cms.common.core.web.domain;

import com.cms.common.core.enums.HttpStatusResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应数据模型类
 *
 * @author 邓志军
 * @date 2024-05-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {
   /**
    * 状态码
    */
   private Integer code;

   /**
    * 返回的消息
    */
   private String msg;

   /**
    * 载荷数据
    */
   private T data;

   public Response(Integer code, String msg) {
       this.code = code;
       this.msg = msg;
   }

   public Response(Integer code, T data) {
       this.code = code;
       this.data = data;
   }

   public Response(Integer code, T data, String msg) {
       this.code = code;
       this.msg = msg;
       this.data = data;
   }

   // 请求成功
   public static <T> Response<T> success(T object) {
       Response<T> ajax = new Response<T>();
       ajax.data = object;
       ajax.code = HttpStatusResponse.SUCCESS.getCode();
       ajax.msg = HttpStatusResponse.SUCCESS.getMsg();
       return ajax;
   }

    // 请求成功
    public static <T> Response<T> success(HttpStatusResponse httpStatusResponse) {
        Response<T> ajax = new Response<T>();
        ajax.code = httpStatusResponse.getCode();
        ajax.msg = httpStatusResponse.getMsg();
        return ajax;
    }

    // 请求成功
    public static <T> Response<T> success() {
        return success(HttpStatusResponse.SUCCESS);
    }

   // 请求成功
   public static <T> Response<T> success(T object, String msg) {
       Response<T> ajax = new Response<T>();
       ajax.code = HttpStatusResponse.SUCCESS.getCode();
       ajax.data = object;
       ajax.msg = msg;
       return ajax;
   }

   public static <T> Response<T> success(Integer code, T object, String msg) {
       Response<T> ajax = new Response<T>();
       ajax.code = code;
       ajax.data = object;
       ajax.msg = msg;
       return ajax;
   }

   // 请求失败
   public static <T> Response<T> error(String msg) {
       Response<T> ajax = new Response<>();
       ajax.msg = msg;
       ajax.code = HttpStatusResponse.SYSTEM_INTERNAL_EXCEPTION.getCode();
       return ajax;
   }

    // 请求失败
    public static <T> Response<T> error() {
        return error(HttpStatusResponse.ERROR);
    }

   public static <T> Response<T> error(Integer code, String msg) {
       Response<T> ajax = new Response<>();
       ajax.code = code;
       ajax.msg = msg;
       return ajax;
   }

   public static <T> Response<T> error(HttpStatusResponse bizCodeEnum) {
       Response<T> ajax = new Response<>();
       ajax.code = bizCodeEnum.getCode();
       ajax.msg = bizCodeEnum.getMsg();
       return ajax;
   }

   public static <T> Response<T> error(Integer code, T object, String msg) {
       Response<T> ajax = new Response<>();
       ajax.code = code;
       ajax.data = object;
       ajax.msg = msg;
       return ajax;
   }
}
