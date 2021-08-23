package com.x930073498.sample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.x930073498.rstore.StoreComponent
import com.x930073498.rstore.withAnchor
import com.x930073498.sample.databinding.ActivityMainBinding

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), StoreComponent {
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setState(binding)
    }

    private fun setState(binding: ActivityMainBinding) {
        withAnchor(viewModel) { scope ->
            with(scope) {
                onInitialized {
                    binding.root.setOnClickListener {
                        data.postValue(++count)
                    }
//                    data.observe(this@MainActivity) {
//                        binding.data.text = "data=$it"
//                    }
                }
                stareAt(::data) {
                    println("enter this line 9999")
                    binding.data.text = "data=${data.value}" }
            }
        }
    }
}