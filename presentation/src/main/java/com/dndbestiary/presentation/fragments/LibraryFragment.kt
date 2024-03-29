package com.dndbestiary.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dndbestiary.databinding.FragmentLibraryBinding
import com.dndbestiary.presentation.MainAdapter
import com.dndbestiary.R
import com.dndbestiary.presentation.viewmodel.MainViewModel
import com.domain.models.DomainPotion

class LibraryFragment : Fragment(), MainAdapter.Listener {
    private lateinit var binding: FragmentLibraryBinding
    private var fragmentCallback: FragmentCallback? = null
    private lateinit var viewModel: MainViewModel
    private val adapter = MainAdapter(this)

    fun setFragmentCallback(callback: FragmentCallback) {
        fragmentCallback = callback
    }

    companion object {
        @JvmStatic
        fun newInstance() = LibraryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding = FragmentLibraryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeToRefresh()
        setupViewModelObserver()
        viewModel.getPotionsFromDb()
    }

    override fun onClick(potionId: String, potionImage: String) {
        fragmentCallback?.sendCallback(
            "openPotionFragment",
            viewModel.getPotionByIdOffline(potionId, potionImage)
        )
    }

    override fun onLikeClick(potion: DomainPotion, isAdd: Boolean) {
        viewModel.deletePotionFromDbByIndex(potion.potionId)
    }

    private fun setupRecyclerView() {
        binding.rvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLibrary.adapter = adapter
    }

    private fun setupViewModelObserver() {
        viewModel.potionListDb.observe(viewLifecycleOwner) { potions ->
            adapter.submitList(potions)
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.layoutBackground
            )
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getPotionsFromDb()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}