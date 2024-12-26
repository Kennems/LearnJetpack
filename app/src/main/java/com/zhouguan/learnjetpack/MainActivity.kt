package com.zhouguan.learnjetpack

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.zhouguan.learnjetpack.ViewModel.MainViewModel
import com.zhouguan.learnjetpack.database.AppDatabase
import com.zhouguan.learnjetpack.databinding.ActivityMainBinding
import com.zhouguan.learnjetpack.entity.Repository
import com.zhouguan.learnjetpack.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

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

//        lifecycle.addObserver(MyObserver())

//        viewModel.counter.observe(this, Observer { count ->
//            mBinding.infoText.text = count.toString()
//        })

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

        }
    }

    override fun onPause() {
        super.onPause()
        sharedPreference.edit()
            .putInt("count_reserved", viewModel.count.value ?: 0)
            .apply() // 或 .commit()
    }
}