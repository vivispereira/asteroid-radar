package com.viv.asteroidradar.presentation.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.viv.asteroidradar.R
import com.viv.asteroidradar.databinding.FragmentMainBinding
import com.viv.asteroidradar.presentation.main.MainAdapter
import com.viv.asteroidradar.presentation.main.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.asteroids.observe(viewLifecycleOwner) {
            val adapter = MainAdapter(MainAdapter.OnClickListener {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.showAsteroidDetails()
            })

            adapter.submitList(it)
            binding.asteroidRecycler.layoutManager = LinearLayoutManager(context)
            binding.asteroidRecycler.adapter = adapter
        }

        viewModel.picture.observe(viewLifecycleOwner) { picture ->
            Picasso.get()
                .load(picture.url)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.placeholder_picture_of_day)
                .into(binding.activityMainImageOfTheDay)
            binding.activityMainImageOfTheDay.contentDescription = picture.title
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.statusLoadingWheel.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.loadAsteroids()
        return binding.root
    }
}
