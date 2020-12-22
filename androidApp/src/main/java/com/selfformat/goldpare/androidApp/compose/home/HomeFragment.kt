package com.selfformat.goldpare.androidApp.compose.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.selfformat.goldpare.androidApp.compose.RootActivity
import com.selfformat.goldpare.androidApp.compose.results.ResultsFragment
import com.selfformat.goldpare.androidApp.compose.theme.GoldpareTheme

class HomeFragment : Fragment() {

    private val model: HomeViewModel by activityViewModels()

    @ExperimentalUnsignedTypes
    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GoldpareTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        HomeView(model)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(model) {
            showResults.observe(viewLifecycleOwner, { state: HomeViewModel.FragState ->
                when (state) {
                    is HomeViewModel.FragState.Loaded -> {
                        if (state.showResult) {
                            showResults()
                        } else {
                            Log.i("Nothing happen", "onViewCreated: ")
                        }
                    }
                }
            })
        }
    }

    private fun showResults() {
        (activity as RootActivity).replaceFragment(fragment = ResultsFragment(), "ResultFragment")
    }
}
