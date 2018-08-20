package com.nd.hilauncherdev.dynamic.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.nd.hilauncherdev.dynamic.PluginContextBase;
import com.nd.hilauncherdev.dynamic.bean.TaskActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射辅助类
 * 
 * @ClassName: LoadInvoker
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lytjackson@gmail.com
 * @date 2013-11-4
 * 
 */
public class LoadInvoker {

	private static final String P_INFO = "invoke";

	/**
	 * 回调activity的OnCreate方法
	 * 
	 * @Title: invokeClientOnCreate
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 * @param savedInstanceState
	 */
	public void invokeClientOnCreate(TaskActivity current_activity, Bundle savedInstanceState) {
		if (null != current_activity) {
			try {
				Method methodonCreate = current_activity.getCurrentClass().getDeclaredMethod("onCreate", new Class[] { Bundle.class });
				methodonCreate.setAccessible(true);
				methodonCreate.invoke(current_activity.getCurrentActivity(), new Object[] { savedInstanceState });
			} catch (NoSuchMethodException e) {
				Log.e(P_INFO, "do not need to call onCreate!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调activity的OnPause方法
	 * 
	 * @Title: invokeClientOnPause
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 */
	public void invokeClientOnPause(TaskActivity current_activity) {
		if (null != current_activity) {
			try {
				Method methodonPause = current_activity.getCurrentClass().getDeclaredMethod("onPause", new Class[] {});
				methodonPause.setAccessible(true);
				methodonPause.invoke(current_activity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onPause!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调activity的OnStart方法
	 * 
	 * @Title: invokeClientOnStart
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 */
	public void invokeClientOnStart(TaskActivity current_activity) {
		if (null != current_activity) {
			try {
				Method methodonStart = current_activity.getCurrentClass().getDeclaredMethod("onStart", new Class[] {});
				methodonStart.setAccessible(true);
				methodonStart.invoke(current_activity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onStart!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调activity的OnResume方法
	 * 
	 * @Title: invokeClientOnResume
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 */
	public void invokeClientOnResume(TaskActivity current_activity) {
		if (null != current_activity) {
			try {
				Method methodOnResume = current_activity.getCurrentClass().getDeclaredMethod("onResume", new Class[] {});
				methodOnResume.setAccessible(true);
				methodOnResume.invoke(current_activity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onResume!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调activity的OnStop方法
	 * 
	 * @Title: invokeClientOnStop
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 */
	public void invokeClientOnStop(TaskActivity current_activity) {
		if (null != current_activity) {
			try {
				Method methodonStop = current_activity.getCurrentClass().getDeclaredMethod("onStop", new Class[] {});
				methodonStop.setAccessible(true);
				methodonStop.invoke(current_activity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onStop!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调activity的OnDestroy方法
	 * 
	 * @Title: invokeClientOnDestroy
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 */
	public void invokeClientOnDestroy(TaskActivity current_activity) {
		if (null != current_activity) {
			try {
				Method methodonDestroy = current_activity.getCurrentClass().getDeclaredMethod("onDestroy", new Class[] {});
				methodonDestroy.setAccessible(true);
				methodonDestroy.invoke(current_activity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onDestroy!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 反射得到指定classname的activity对象
	 * 
	 * @Title: invokeClientActivityInstance
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param loadClass
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
/*	public Object invokeClientActivityInstance(Class<?> loadClass) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = loadClass.getConstructor(new Class[] {});
		Object instance = constructor.newInstance(new Object[] {});
		return instance;
	}*/

	/**
	 * 回调activity的onRestart方法
	 * 
	 * @Title: invokeClientOnRestart
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 */
	public void invokeClientOnRestart(TaskActivity current_activity) {
		if (null != current_activity) {
			try {
				Method methodonRestart = current_activity.getCurrentClass().getDeclaredMethod("onRestart", new Class[] {});
				methodonRestart.setAccessible(true);
				methodonRestart.invoke(current_activity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onRestart!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调activity的onNewIntent方法
	 * 
	 * @Title: invokeClientOnNewIntent
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 * @param intent
	 */
	public void invokeClientOnNewIntent(TaskActivity current_activity, Intent intent) {
		if (null != current_activity) {
			try {
				Method methodonNewIntent = current_activity.getCurrentClass().getDeclaredMethod("onNewIntent", new Class[] { Intent.class });
				methodonNewIntent.setAccessible(true);
				methodonNewIntent.invoke(current_activity.getCurrentActivity(), new Object[] { intent });
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onNewIntent!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调activity的onKeyDown方法
	 * 
	 * @Title: invokeClientOnKeyDown
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param current_activity
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public boolean invokeClientOnKeyDown(TaskActivity current_activity, int arg0, KeyEvent arg1) {
		if (null != current_activity) {
			try {
				boolean result = false;
				Method methodOnKeyDown = current_activity.getCurrentClass().getMethod("onKeyDown", new Class[] { Integer.TYPE, KeyEvent.class });
				methodOnKeyDown.setAccessible(true);
				result = (Boolean) methodOnKeyDown.invoke(current_activity.getCurrentActivity(), new Object[] { arg0, arg1 });
				return result;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				Log.i(P_INFO, "do not need to call invokeClientOnKeyDown!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void invokeClientOnWindowFocusChanged(TaskActivity current_activity, boolean hasFocus) {
		if (null != current_activity) {
			try {
				Method methodonWindowFocusChanged = current_activity.getCurrentClass().getDeclaredMethod("onWindowFocusChanged", new Class[] { Boolean.TYPE });
				methodonWindowFocusChanged.setAccessible(true);
				methodonWindowFocusChanged.invoke(current_activity.getCurrentActivity(), new Object[] { hasFocus });
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onWindowFocusChanged!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void invokeClientOnSaveInstanceState(TaskActivity current_activity, Bundle outState) {
		if (null != current_activity) {
			try {
				Method methodonSaveInstanceState = current_activity.getCurrentClass().getDeclaredMethod("onSaveInstanceState", new Class[] { Bundle.class });
				methodonSaveInstanceState.setAccessible(true);
				methodonSaveInstanceState.invoke(current_activity.getCurrentActivity(), new Object[] { outState });
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onSaveInstanceState!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void invokeClientOnRestoreInstanceState(TaskActivity current_activity, Bundle savedInstanceState) {
		if (null != current_activity) {
			try {
				Method methodonRestoreInstanceState = current_activity.getCurrentClass().getDeclaredMethod("onRestoreInstanceState", new Class[] { Bundle.class });
				methodonRestoreInstanceState.setAccessible(true);
				methodonRestoreInstanceState.invoke(current_activity.getCurrentActivity(), new Object[] { savedInstanceState });
			} catch (NoSuchMethodException e) {
				// ignore
				Log.i(P_INFO, "do not need to call onRestoreInstanceState!!!");
			} catch(BadParcelableException e){
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void invokeClientOnPostCreate(TaskActivity current_activity, Bundle savedInstanceState) {
		if (null != current_activity) {
			try {
				Method methodonPostCreate = current_activity.getCurrentClass().getDeclaredMethod("onPostCreate", new Class[] { Bundle.class });
				methodonPostCreate.setAccessible(true);
				methodonPostCreate.invoke(current_activity.getCurrentActivity(), new Object[] { savedInstanceState });
			} catch (NoSuchMethodException e) {
				// ignore
				Log.i(P_INFO, "do not need to call onPostCreate!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

/*	public void invokeClientOnPostResume(TaskActivity current_activity) {
		if (null != current_activity) {
			try {
				Method methodonPostResume = current_activity.getCurrentClass().getDeclaredMethod("onPostResume", new Class[] {});
				methodonPostResume.setAccessible(true);
				methodonPostResume.invoke(current_activity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				// ignore
				Log.i(P_INFO, "do not need to call onPostResume!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

	public void invokeClientOnActivityResult(TaskActivity current_activity, int requestCode, int resultCode, Intent data) {
		if (null != current_activity) {
			try {
				Method methodOnActivityResult = current_activity.getCurrentClass().getDeclaredMethod("onActivityResult", new Class[] { Integer.TYPE, Integer.TYPE, Intent.class });
				methodOnActivityResult.setAccessible(true);
				methodOnActivityResult.invoke(current_activity.getCurrentActivity(), new Object[] { requestCode, resultCode, data });
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onActivityResult!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

/*
	public boolean invokeClientOnKeyLongPress(TaskActivity current_activity, int keyCode, KeyEvent event) {
		if (null != current_activity) {
			try {
				boolean result = false;
				Method methodonKeyLongPress = current_activity.getCurrentClass().getMethod("onKeyLongPress", new Class[] { Integer.TYPE, KeyEvent.class });
				methodonKeyLongPress.setAccessible(true);
				result = (Boolean) methodonKeyLongPress.invoke(current_activity.getCurrentActivity(), new Object[] { keyCode, event });
				return result;
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onKeyLongPress!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
*/

/*	public boolean invokeClientOnKeyUp(TaskActivity current_activity, int keyCode, KeyEvent event) {
		if (null != current_activity) {
			try {
				boolean result = false;
				Method methodonKeyUp = current_activity.getCurrentClass().getMethod("onKeyUp", new Class[] { Integer.TYPE, KeyEvent.class });
				methodonKeyUp.setAccessible(true);
				result = (Boolean) methodonKeyUp.invoke(current_activity.getCurrentActivity(), new Object[] { keyCode, event });
				return result;
			} catch (NoSuchMethodException e) {
				Log.i(P_INFO, "do not need to call onKeyUp!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}*/

	public Object invokeApplication(ClassLoader classLoader, String appClassName, PluginContextBase pluginContext) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Object application = null;
		Class<?> appClass = classLoader.loadClass(appClassName);
		Constructor<?> appConstructor = appClass.getConstructor(new Class[] {});
		application = appConstructor.newInstance(new Object[] {});

		Method methodAppInit = appClass.getMethod("initContext", new Class[] { Context.class });
		methodAppInit.setAccessible(true);
		methodAppInit.invoke(application, new Object[] { pluginContext });

		Method methodOnCreate = appClass.getMethod("onCreate", new Class[] {});
		methodOnCreate.setAccessible(true);
		methodOnCreate.invoke(application, new Object[] {});

		return application;
	}

	public void invokeActivity(Activity pluginLoaderActivity, Class<?> activityClass, Object activity, Object application, PluginContextBase pluginContext, String pkgName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		try {
			Field applicationField = PluginLoaderUtil.getClassField(activityClass, "mApplication");
			if (null != applicationField) {
				applicationField.setAccessible(true);
				applicationField.set(activity, application);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        //执行插件中的这些构造方法
		Method methodInit = activityClass.getMethod("initContext", new Class[] { Context.class });
		methodInit.setAccessible(true);
		methodInit.invoke(activity, new Object[] { pluginContext });

		Method methodSetActivity = activityClass.getMethod("setParentContext", new Class[] { Activity.class });
		methodSetActivity.setAccessible(true);
		methodSetActivity.invoke(activity, new Object[] { pluginLoaderActivity });

		Field pkgField = PluginLoaderUtil.getClassField(activityClass, "mPluginPackageName");
		if (null != pkgField) {
			pkgField.setAccessible(true);
			pkgField.set(activity, pkgName);
		}
	}
}
