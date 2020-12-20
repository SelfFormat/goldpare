package com.selfformat.goldpare.androidApp.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.selfformat.goldpare.androidApp.compose.ui.GoldpareTheme

class HomeFragment : Fragment() {

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
                        HomeView()
                    }
                }
            }
        }
    }
}
