package com.dndbestiary.searchfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dndbestiary.FragmentCallback
import com.dndbestiary.databinding.FragmentSearchBinding

class PotionFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var fragmentCallback: FragmentCallback? = null

    fun setFragmentCallback(callback: FragmentCallback){
        fragmentCallback = callback
    }
    companion object {
        @JvmStatic
        fun newInstance() = PotionFragment()
    }

    private lateinit var viewModel: PotionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}