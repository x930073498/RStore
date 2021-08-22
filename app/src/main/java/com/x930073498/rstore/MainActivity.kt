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
                    binding.root.setOnClickListener {
                       viewModel.data="${viewModel.count++}"
                    }
                }
                stareAt(::data) {
                    println("enter this line data=$this")
                    binding.data.text = "data=$data"

                }
                stareAt(::count){
                    binding.count.text="count=$this"
                    println("enter this line count=$this")
                }
//                stareAt(::data,::count){
//
//
//                }
            }
        }
    }

}