package com.x930073498.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.x930073498.rstore.*
import com.x930073498.rstore.core.*
import com.x930073498.rstore.property.lazyField
import com.x930073498.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), StoreComponent {
    private val viewModel by savedStateViewModels<MainViewModel>()

    private val adapter by lazyField {
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
                        ++count
                    }
                }
                stareAt(::countOb) {
                    println("enter this line countOb=$this")
                    binding.data.text = "data=$this"
                    stareAt(::count) {

                    }
                }
                stareAt(::list) {
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