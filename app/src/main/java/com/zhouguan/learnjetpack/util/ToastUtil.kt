package com.zhouguan.learnjetpack.util

import android.content.Context
import android.widget.Toast
import com.zhouguan.learnjetpack.MyApplication

class ToastUtil {
}

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApplication.context, this, duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApplication.context, this, duration).show()
}
