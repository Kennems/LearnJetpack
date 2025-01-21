package com.zhouguan.learnjetpack

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

// SimpleWorker 类用于在后台任务中执行某些操作，它继承自 Worker 类。
// Worker 是 Android WorkManager 提供的基类，用于定义定时或异步任务。
class SimpleWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    // 伴生对象，用于定义静态常量和方法。Kotlin 中的 companion object 类似于 Java 的静态成员。
    companion object {
        // 日志标签，用于标识日志来源，便于调试时筛选相关日志信息。
        const val TAG = "SimpleWorker"
    }

    // 重写 doWork 方法，这是 Worker 的核心方法。
    // doWork 方法在后台线程中运行，定义需要执行的任务逻辑。
    override fun doWork(): Result {
        // 输出一条调试日志，标识当前任务的执行情况。
        Log.d(TAG, "doWork in SimpleWorker")

        // 返回任务执行的结果，这里表示任务成功完成。
        // Result.success(): 表示任务成功完成，后续不需要重试。
        return Result.success()

        // 如果需要明确标识任务失败，可以返回 Result.failure()。
        // Result.failure(): 表示任务失败，任务不会再尝试重试。
//        return Result.failure()

        // 如果任务执行失败，但希望 WorkManager 重试，可以返回 Result.retry()。
        // Result.retry(): 表示任务暂时失败，但 WorkManager 会根据重试策略再次尝试执行。
//        return Result.retry()
    }
}
