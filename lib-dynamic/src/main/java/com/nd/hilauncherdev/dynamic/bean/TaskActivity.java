package com.nd.hilauncherdev.dynamic.bean;

/**
 * activity堆栈信息中存放的实体
 * @ClassName: TaskActivity
 * @author lytjackson@gmail.com
 * @date 2013-11-4
 *
 */
public class TaskActivity {
	private Object currentActivity;// activity实体
	private Class<?> currentClass;// activity的class对象

	public Object getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Object currentActivity) {
		this.currentActivity = currentActivity;
	}

	public Class<?> getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(Class<?> curr_class) {
		this.currentClass = curr_class;
	}
}