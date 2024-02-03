package com.dndbestiary.mainfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.dndbestiary.FragmentCallback
import com.dndbestiary.databinding.FragmentMainBinding
import com.hfad.data.retrofit.ApiClient
import com.hfad.data.retrofit.ApiService
import com.hfad.data.retrofit.Potion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment(), MainAdapter.Listener {
    private lateinit var binding: FragmentMainBinding
    private var fragmentCallback: FragmentCallback? = null

    fun setFragmentCallback(callback: FragmentCallback){
        fragmentCallback = callback
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MainAdapter(this)
        binding.rvMain.layoutManager = GridLayoutManager(requireContext(),3)
        binding.rvMain.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            getData(adapter,false)
        }
        getData(adapter, true)
    }

    private fun getData(adapter: MainAdapter, visibility: Boolean){
        if(visibility){
            binding.progressBar.visibility = View.VISIBLE
        }
        CoroutineScope(Dispatchers.IO).launch {
            val potionsList = ApiClient().getClient().create(ApiService::class.java).getPotions()
            withContext(Dispatchers.Main){
                adapter.submitList(potionsList.potions)
                binding.progressBar.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    override fun onClick(potionId: String) {
        ApiClient().resetClient()
        fragmentCallback?.sendCallback("openPotionFragment", potionId)
    }
}