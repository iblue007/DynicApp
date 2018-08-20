package com.nd.hilauncherdev.dynamic.bean;

/**
 * service堆栈信息中存放的实体
 * 
 * @ClassName: TaskService
 * @author lytjackson@gmail.com
 * @date 2013-11-4
 * 
 */
public class TaskService {
	private Object currentService;// service实体
	private Class<?> currentClass;// service的class对象

	public Object getCurrentService() {
		return currentService;
	}

	public void setCurrentService(Object currentService) {
		this.currentService = currentService;
	}

	public Class<?> getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(Class<?> currentClass) {
		this.currentClass = currentClass;
	}

}