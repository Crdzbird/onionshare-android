package org.onionshare.android.ui

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import org.onionshare.android.R
import org.onionshare.android.ui.theme.OnionshareTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnionshareTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainUi(
                        stateFlow = viewModel.shareState,
                        onFabClicked = this::onFabClicked,
                        onFileRemove = viewModel::removeFile,
                        onRemoveAll = viewModel::removeAll,
                        onSheetButtonClicked = viewModel::onSheetButtonClicked,
                    )
                }
            }
        }
    }

    private val launcher = registerForActivityResult(GetMultipleContents()) { uris ->
        viewModel.onUrisReceived(uris)
    }

    private fun onFabClicked() {
        try {
            launcher.launch("*/*")
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.add_files_not_supported, LENGTH_SHORT).show()
        }
    }
}
