package com.mwishen.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mwishen.myapplication.PostsAdapter
import com.mwishen.myapplication.R
import com.mwishen.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //we declare a recycler view and access to PostAdapter
    //The Post adapter defines how single product will be shown
    //The recycler view loads loops all product to show them

    //create a recycler view and find/init it later
    private lateinit var recyclerPosts: RecyclerView
    //create a reference to your adapter , make it empty for now
    private var mAdapter: PostsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //**********

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}