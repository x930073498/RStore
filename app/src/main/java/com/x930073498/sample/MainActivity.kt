package com.x930073498.sample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.x930073498.rstore.*
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
            withProperty(::count) {
                Toast.makeText(this@MainActivity, "count=$this", Toast.LENGTH_SHORT).show()
            }
            withAnchor(starter = LifecycleAnchorStarter()) {
                with(it) {
                    stareAt(::data) {
                        println("data=${data.value}")
                    }
                }
            }
        }

        withAnchor(viewModel) { scope ->
            with(scope) {
                onInitialized {
                    binding.root.setOnClickListener {
                        data.tryEmit(++count)
                    }
                }
                stareAt(::data) {
                    binding.data.text = "data=${data.value}"
                }
                stareAt(::list) {
                    println(this)
                }
            }
        }
    }
}