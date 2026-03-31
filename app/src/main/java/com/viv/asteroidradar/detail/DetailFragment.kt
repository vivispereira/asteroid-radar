package com.viv.asteroidradar.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.viv.asteroidradar.R
import com.viv.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        arguments?.let {
            val asteroid = DetailFragmentArgs.fromBundle(it).selectedAsteroid
            binding.asteroid = asteroid
            binding.helpButton.setOnClickListener {
                displayAstronomicalUnitExplanationDialog()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Set up the action bar with navigation support
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onResume() {
        super.onResume()
        
        // Ensure back button handler is set up
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
                .setMessage(getString(R.string.astronomica_unit_explanation))
                .setPositiveButton(android.R.string.ok, null)
            builder.create().show()
        }

    }
}
