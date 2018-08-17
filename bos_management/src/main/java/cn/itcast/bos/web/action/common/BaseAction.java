package cn.itcast.bos.web.action.common;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	protected T model ;
	@Override
	public T getModel() {
		return model;
	}
	
	protected Integer page;
	protected Integer rows;
	
	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public BaseAction(){
		
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		
		 Class<T> clazz =(Class<T>)type.getActualTypeArguments()[0];
		 
		 try {
			model =  clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 转换成分页json
	 * @param pageData
	 * @return
	 */
	public String pageJson(Page<T> pageData) {
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getNumberOfElements());
		map.put("rows", pageData.getContent());
		
		//序列化
		String jsonString = JSON.toJSONString(map);
		return jsonString;
	}
	
	/**
	 * 将json数据响应回页面
	 * @param json
	 * @throws IOException
	 */
	public void resposeToFront(String json) throws IOException {
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().println(json);
	}

}
