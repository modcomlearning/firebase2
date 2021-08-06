package com.mwishen.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.mwishen.myapplication.Post
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
        //find the recycler in XML fragment_home.xml
        recyclerPosts = root.findViewById(R.id.recyclerPosts)
        loadPosts()//call load post function
        return root
    }//oncreate ends here
    private fun loadPosts() {
        val query= FirebaseDatabase.getInstance().reference.child("MODCOM").child("POSTS")
        val options = FirebaseRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
        //options has the data from firebase which have title , desc, image, timestamp
        mAdapter = PostsAdapter(options) //pass data to the adapter
        //since the adapter will only show one product, apply the adapter to
        //recycler view so that it can loop all other products
        recyclerPosts.apply {
            //the recycler will display other products as a GRID of 2 columns
            layoutManager = GridLayoutManager(activity,2)
            adapter = mAdapter
        }
        //stop progress here
    }//end load posts
    override fun onStart() {
        super.onStart()
        mAdapter!!.startListening()
    }//make it listen in real time

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}