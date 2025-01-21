package com.zhouguan.learnjetpack

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.zhouguan.learnjetpack.ViewModel.MainViewModel
import com.zhouguan.learnjetpack.database.AppDatabase
import com.zhouguan.learnjetpack.databinding.ActivityMainBinding
import com.zhouguan.learnjetpack.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val sharedPreference: SharedPreferences by lazy {
        getPreferences(MODE_PRIVATE)
    }

    private val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val countReserved by lazy {
        sharedPreference.getInt("count_reserved", 0)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(counterReserved = countReserved)
        ).get(MainViewModel(0)::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycle.addObserver(MyObserver(lifecycle))

        viewModel.run {
            count.observe(this@MainActivity) { count ->
                mBinding.infoText.text = count.toString()
            }
            user.observe(this@MainActivity, Observer { user ->
                mBinding.infoText.text = user.firstName
            })
        }

        val userDao = AppDatabase.getDatabase(this).userDao()
        val user1 = User("Tom", "Brady", 40)
        val user2 = User("Tom", "Hanks", 63)

        mBinding.run {
            plusOneBtn.setOnClickListener {
                viewModel.plusOne()
            }

            clearBtn.setOnClickListener {
                viewModel.clear()
            }

            getUserBtn.setOnClickListener {
                val userId = (0..10000).random().toString()
                viewModel.getUser(userId)
            }

            addDataBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    user1.id = userDao.insertUser(user1)
                    user2.id = userDao.insertUser(user2)
                }
            }

            updateDataBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    user1.age = 42
                    userDao.updateUser(user1)
                }
            }

            deleteDataBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.deleteUserByLastName("Hanks")
                }
            }

            queryDataBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val users = userDao.loadAllUsers()
                    users.forEach { user ->
                        Log.d("MainActivity", user.toString())
                    }
                }
            }

            // 设置按钮点击事件监听器，当用户点击 doWorkBtn 时触发以下逻辑。
            doWorkBtn.setOnClickListener {
                // 创建一个一次性 WorkRequest，用于调度一个后台任务。
                // 使用 SimpleWorker 类作为任务逻辑的实现类。

                // 设置 WorkRequest：配置延迟、标记和退避策略。
                val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                    // 设置任务的初始延迟时间，这里设置为 5 分钟。
                    .setInitialDelay(5, TimeUnit.MINUTES)
                    // 给任务添加一个标签，用于分组管理任务或批量取消。
                    .addTag("simple")
                    // 设置任务的退避策略，防止任务频繁失败时过度消耗资源。
                    // BackoffPolicy.LINEAR 表示线性增长的延迟重试，延迟初始时间为 10 秒。
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                    // 构建最终的 WorkRequest 对象。
                    .build()

                // 使用 WorkManager 将任务请求添加到任务队列中，开始调度任务。
                WorkManager.getInstance(this@MainActivity).enqueue(request)

                // 获取任务信息的 LiveData，通过任务的 ID 查询状态。
                val workInfo =
                    WorkManager.getInstance(this@MainActivity).getWorkInfoByIdLiveData(request.id)

                Log.d(TAG, "workInfo: $workInfo ")

                // 根据任务标签取消所有标记为 "simple" 的任务。
//                WorkManager.getInstance(this@MainActivity).cancelAllWorkByTag("simple")

                // 根据任务 ID 取消具体的任务。
//                WorkManager.getInstance(this@MainActivity).cancelWorkById(request.id)

                // 取消所有已经排队的任务（慎用，会影响所有正在运行或计划的任务）。
//                WorkManager.getInstance(this@MainActivity).cancelAllWork()

                // 观察任务的运行状态，通过任务 ID 获取任务的 LiveData。
                WorkManager.getInstance(this@MainActivity)
                    .getWorkInfoByIdLiveData(request.id)
                    .observe(this@MainActivity) { workInfo ->
                        // 当任务状态改变时，会回调到此处。
                        if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                            // 任务成功时，打印成功日志。
                            Log.d(TAG, "do work succeeded")
                        } else if (workInfo?.state == WorkInfo.State.FAILED) {
                            // 任务失败时，打印失败日志。
                            Log.d(TAG, "do work failed")
                        }
                    }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sharedPreference.edit()
            .putInt("count_reserved", viewModel.count.value ?: 0)
            .apply() // 或 .commit()
    }
}