package com.zhouguan.learnjetpack

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * MyObserver 是一个实现 LifecycleObserver 接口的类，用于观察生命周期变化
 * 并响应生命周期事件，如 ON_START 和 ON_STOP。它可以用于 Activity 或 Fragment
 * 等组件中来监听生命周期事件并执行相应操作。
 *
 * @param lifecycle Activity 或其他组件的生命周期对象，用于注册和管理生命周期事件。
 */
class MyObserver(val lifecycle: Lifecycle) : LifecycleObserver {

    companion object {
        // 定义一个静态常量 TAG，用于日志输出时标识当前类
        private const val TAG = "MyObserver"
    }

    /**
     * 当生命周期事件为 ON_START 时调用，表示组件进入前台并开始交互。
     * 这个方法会在组件被启动并可见时执行。
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityStart() {
        // 使用 Log.d 输出日志信息，标识组件已经开始
        Log.d(TAG, "activityStart: ")
    }

    /**
     * 当生命周期事件为 ON_STOP 时调用，表示组件离开前台并进入后台。
     * 这个方法会在组件不再可见或被销毁前调用。
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityStop() {
        // 使用 Log.d 输出日志信息，标识组件已经停止
        Log.d(TAG, "activityStop: ")
    }
}
