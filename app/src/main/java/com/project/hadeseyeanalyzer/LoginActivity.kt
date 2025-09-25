package com.project.hadeseyeanalyzer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.hadeseyeanalyzer.databinding.ActivityLoginBinding
import com.project.hadeseyeanalyzer.dialog.ShowDialog
import kotlinx.coroutines.Runnable

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private var dialog = ShowDialog(this)
    private var isPasswordVisible = true
    private var isConfirmPasswordVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordToggle(binding.passwordInput, true)
        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleIcon.setOnClickListener {
            signInWithGoogle()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isEmpty()) {
                dialog.invalidDialog("Error", "Please fill the email")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                dialog.invalidDialog("Error", "Please fill the password")
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        dialog.successDialog(
                            "Success", "Login Successfully", "OK",
                            Runnable {
                                intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            })
                    } else {
                        dialog.invalidDialog("Error", "invalid username or password")
                    }
                }
        }
    }

    private fun setupPasswordToggle(editText: EditText, isMainPassword: Boolean) {
        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                if (event.rawX >= (editText.right - editText.compoundDrawables[drawableEnd].bounds.width())) {
                    if (isMainPassword) {
                        isPasswordVisible = !isPasswordVisible
                        togglePasswordVisibility(editText, isPasswordVisible)
                    } else {
                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                        togglePasswordVisibility(editText, isConfirmPasswordVisible)
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility(editText: EditText, visible: Boolean) {
        if (visible) {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_closed, 0)
        }
        editText.setSelection(editText.text.length)
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
            dialog.invalidDialog("Error", "Failed to signup")
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        Log.d("LoginActivity", "Updating UI with account: ${account.email}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                dialog.invalidDialog("Error", "Failed to Login")
            }
        }
    }
}