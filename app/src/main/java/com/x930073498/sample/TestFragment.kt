package com.x930073498.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.x930073498.rstore.core.LifecycleAnchorStarter
import com.x930073498.rstore.core.StoreComponent
import com.x930073498.rstore.core.onInitialized
import com.x930073498.rstore.core.stareAt
import com.x930073498.sample.databinding.FragmentTestBinding

class TestFragment : Fragment(R.layout.fragment_test), StoreComponent {

    private val viewModel by viewModels<MainViewModel>()

    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt("position", position) ?: 0
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewBinding = FragmentTestBinding.bind(view)

        viewModel.withAnchor(starter = LifecycleAnchorStarter(viewModel.data.value > 0)) {
            with(it) {
                onInitialized {
                    viewBinding.root.setOnClickListener {
                        data.tryEmit(++count)
                    }
                }
                stareAt(::data) {
                    viewBinding.data.text = "data $position =${data.value}"
                }
            }
        }

    }
}