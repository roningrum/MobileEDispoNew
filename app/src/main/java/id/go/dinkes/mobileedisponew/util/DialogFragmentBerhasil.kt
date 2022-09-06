package id.go.dinkes.mobileedisponew.util

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.FragmentDialogBerhasilBinding
import kotlin.concurrent.thread

class DialogFragmentBerhasil : DialogFragment() {
    private var isAnimating: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDialogBerhasilBinding.inflate(inflater, container, false)
//        binding.checkView.setAnimation("success.json")
        isAnimating = true
        thread {
            while (isAnimating) {
                Thread.sleep(1000)
                activity?.runOnUiThread {
                    binding.checkView.cancelAnimation() // Resume your animation from 50% to 100%
                }
                isAnimating = false
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}