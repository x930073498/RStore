package com.x930073498.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.x930073498.rstore.*
import com.x930073498.rstore.core.*
import com.x930073498.rstore.property.lazyField
import com.x930073498.rstore.util.AwaitState
import com.x930073498.rstore.util.awaitState
import com.x930073498.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), StoreComponent, TestFeature {
    private val viewModel by savedStateViewModels<MainViewModel>()

    private val listViewModel by viewModels<TestListViewModel>()
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
//        with(listViewModel) {
//            withAnchor(LifecycleAnchorStarter { goods.isNotEmpty() }) {
//                onInitialized {
//                    if (goods.isEmpty()) isRefresh = true
//                }
//                stareAt(::isRefresh) {
//                    println("enter this line 79")
//                    if (this) refresh()
//                }
//                stareAt(::goods) {
//                    println("enter this line $this")
//                }
//            }
//        }

        with(viewModel) {
            setFeature(::count, Feature.Anchor)
            withProperty(::count) {
                println("enter this line count activity=$this")
            }
            withAnchor {
                onInitialized {
                    val awaitState = AwaitState.create(false)
                    binding.root.setOnClickListener {
                        startActivity(Intent(this@MainActivity, SecondActivity::class.java))
//                        awaitState.setState(!awaitState.state)
                    }
                    launchOnIO {
                        while (isActive) {
                            awaitState.awaitState(true)
                            delay(400)
                            count++
                        }
                    }

                }
                stareAt(::data) {
//                    println("enter this line countOb=$this")
                    binding.data.text = "data=$value"
                }
                stareAt(::count) {
                    println("enter this line count=$this")
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

            Toast.makeText(this@MainActivity, "data ????????????5", Toast.LENGTH_SHORT).show()
        }

    }

    override fun print() {
        println("enter this line activity=$this")
    }


}