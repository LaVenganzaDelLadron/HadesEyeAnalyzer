package com.project.hadeseyeanalyzer

import android.accounts.Account
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.project.hadeseyeanalyzer.databinding.ActivityDashboardBinding
import com.project.hadeseyeanalyzer.fragments.AccountFragment
import com.project.hadeseyeanalyzer.fragments.HomeFragment
import com.project.hadeseyeanalyzer.fragments.ReportFragment
import com.project.hadeseyeanalyzer.fragments.ScanFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.scan -> replaceFragment(ScanFragment())
                R.id.report -> replaceFragment(ReportFragment())
                R.id.account -> replaceFragment(AccountFragment())
            }
            true
        }




    }
    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}