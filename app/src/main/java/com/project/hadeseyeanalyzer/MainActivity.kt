package com.project.hadeseyeanalyzer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.hadeseyeanalyzer.databinding.ActivityLoginBinding
import com.project.hadeseyeanalyzer.databinding.ActivityMainBinding
import com.project.hadeseyeanalyzer.dialog.ShowDialog

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private var dialog = ShowDialog(this)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java);
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent)
        }

        binding.googleIcon.setOnClickListener{
            signInWithGoogle()
        }

    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResultTask(task)
        }
    }

    private fun handleResultTask(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Log.w("LoginActivity", "Google Sign-In failed", task.exception)
            dialog.invalidDialog("Error", "Failed to signup. Check logs for details.")
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        Log.d("LoginActivity", "Updating UI with account: ${account.email}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.w("LoginActivity", "Firebase credential sign-in failed", task.exception)
                dialog.invalidDialog("Error", "Failed to Login. Check logs for details.")
            }
        }
    }
}