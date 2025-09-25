package com.project.hadeseyeanalyzer.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ViewFlipper
import com.project.hadeseyeanalyzer.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ScanFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewFlipper: ViewFlipper
    private val VIEW_FILE_SCAN = 0
    private val VIEW_URL_SCAN = 1
    private val VIEW_BREACH_SCAN = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)
        viewFlipper = view.findViewById(R.id.viewFlipper)

        viewFlipper.displayedChild = VIEW_FILE_SCAN

        val scanFile = view.findViewById<Button>(R.id.btnFileScan)
        val scanUrl = view.findViewById<Button>(R.id.btnUrlScan)
        val scanBreach = view.findViewById<Button>(R.id.btnBreachCheck)

        scanFile.setOnClickListener {
            if (viewFlipper.displayedChild != VIEW_FILE_SCAN) {
                viewFlipper.displayedChild = VIEW_FILE_SCAN
            }
        }

        scanUrl.setOnClickListener {
            if (viewFlipper.displayedChild != VIEW_URL_SCAN) {
                viewFlipper.displayedChild = VIEW_URL_SCAN
            }
        }

        scanBreach.setOnClickListener {
            if (viewFlipper.displayedChild != VIEW_BREACH_SCAN) {
                viewFlipper.displayedChild = VIEW_BREACH_SCAN
            }
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
