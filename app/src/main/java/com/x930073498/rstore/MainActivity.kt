package com.x930073498.rstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.x930073498.rstore.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity(), StoreComponent {
    val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.withAnchorStateChanged {
            with(it) {
                onInitialized {
                    viewModel.changeProperty()
                    binding.root.setOnClickListener {
                        viewModel.changeProperty()
                    }
                }
                stareAt(::data) {
                    println("enter this line data=$this")
                    binding.tv.text = this
                }
            }
        }
    }

}