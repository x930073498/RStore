package com.x930073498.rstore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.x930073498.rstore.databinding.ActivityMainBinding
@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), StoreComponent {
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setState(binding)
    }
    private fun setState(binding:  ActivityMainBinding) {
        viewModel.withAnchor {
            with(it) {
                onInitialized {
                    binding.root.setOnClickListener { data = "${++count}" }
                }
                stareAt(::data) { binding.data.text = "data=$data" }
            }
        }
    }
}