package com.x930073498.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.x930073498.rstore.*
import com.x930073498.rstore.core.LifecycleAnchorStarter
import com.x930073498.rstore.core.StoreComponent
import com.x930073498.rstore.core.onInitialized
import com.x930073498.rstore.core.stareAt
import com.x930073498.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), StoreComponent {
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private val adapter by lazy {
        MainFragmentFragmentStateAdapter(supportFragmentManager, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = adapter
        setState(binding)
    }

    private fun setState(binding: ActivityMainBinding) {
        with(viewModel) {
            withAnchor(starter = LifecycleAnchorStarter()) {
                onInitialized {
                    binding.root.setOnClickListener {
                        data.tryEmit(++count)
                    }
                }
                stareAt(::data) {
                    println("data=${data.value}")
                    binding.data.text="data=${data.value}"
                }
                stareAt(::list){
                    println("enter this line list=$this")
                }
            }
        }
        lifecycleScope.launch {
            with(viewModel) {
                awaitUntil(::data) {
                    value > 5
                }
            }
            Toast.makeText(this@MainActivity, "data 数值大于5", Toast.LENGTH_SHORT).show()
        }

    }
}