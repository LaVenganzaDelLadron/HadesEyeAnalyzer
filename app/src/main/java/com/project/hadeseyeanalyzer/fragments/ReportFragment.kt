package com.project.hadeseyeanalyzer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ViewFlipper
import com.project.hadeseyeanalyzer.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewFlipper: ViewFlipper
    private val VIEW_ALL_SAFE = 0
    private val VIEW_SAFE = 1
    private val VIEW_THREAT = 2
    private val VIEW_MALICIOUS = 3
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
        val view = inflater.inflate(R.layout.fragment_report, container, false)
        viewFlipper = view.findViewById(R.id.viewFlipper)

        viewFlipper.displayedChild = VIEW_ALL_SAFE

        val allFilter = view.findViewById<Button>(R.id.btnAll)
        val safeFilter = view.findViewById<Button>(R.id.btnSafe)
        val threatFilter = view.findViewById<Button>(R.id.btnThreat)
        val maliciousFilter = view.findViewById<Button>(R.id.btnMalicious)


        allFilter.setOnClickListener {
            if (viewFlipper.displayedChild != VIEW_ALL_SAFE) {
                viewFlipper.displayedChild = VIEW_ALL_SAFE
            }
        }

        safeFilter.setOnClickListener {
            if (viewFlipper.displayedChild != VIEW_SAFE) {
                viewFlipper.displayedChild = VIEW_SAFE
            }
        }

        threatFilter.setOnClickListener {
            if (viewFlipper.displayedChild != VIEW_THREAT) {
                viewFlipper.displayedChild = VIEW_THREAT
            }
        }

        maliciousFilter.setOnClickListener {
            if (viewFlipper.displayedChild != VIEW_MALICIOUS) {
                viewFlipper.displayedChild = VIEW_MALICIOUS
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}