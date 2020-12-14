package com.martin6699s.springBootExample2021.vo.resp;
import com.martin6699s.springBootExample2021.constant.enums.ResponseCodeEnum;

public class BaseResponse<T> {

	private Integer code;

	private String message;

	private T data;


	public BaseResponse<T> setSuccess(T data) {

		this.data = data;
		this.code = ResponseCodeEnum.SUCCESS.getCode();
		this.message = ResponseCodeEnum.SUCCESS.getMessage();
		return this;
	}

	public BaseResponse<T> setSuccess(T data,String message) {

		this.data = data;
		this.message = message;
		this.code = ResponseCodeEnum.SUCCESS.getCode();
		return this;
	}




	public BaseResponse setSuccess() {
		this.code = ResponseCodeEnum.SUCCESS.getCode();
		this.message = ResponseCodeEnum.SUCCESS.getMessage();
		return this;
	}

	/**
	 * 设置响应的错误信息
	 */
	public BaseResponse setError(Integer code, String message) {

		this.code = code;
		this.message = message;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
