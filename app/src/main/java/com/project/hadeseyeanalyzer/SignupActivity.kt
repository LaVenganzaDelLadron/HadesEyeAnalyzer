package com.project.hadeseyeanalyzer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.hadeseyeanalyzer.databinding.ActivitySignupBinding
import com.project.hadeseyeanalyzer.dialog.ShowDialog
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var isPasswordVisible = true
    private var isConfirmPasswordVisible = true
    private var dialog = ShowDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        setupPasswordToggle(binding.passwordInput, true)
        setupPasswordToggle(binding.confirmPasswordInput, true)


        binding.googleIcon.setOnClickListener{
            signInWithGoogle()
        }

        binding.signupButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (!isEmailValid(email)) {
                dialog.invalidDialog("Error","invalid email")
                return@setOnClickListener
            }
            if (!isPasswordValid(password)) {
                dialog.invalidDialog("Error","password must contains character, number and symbols")
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                dialog.invalidDialog("Error", "password doesn't match")
                return@setOnClickListener
            }

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            dialog.successDialog("Success", "Signup Successfully", "OK",
                                Runnable {
                                    intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                })
                        }else{
                            dialog.invalidDialog("Failed", "Failed to Signup")
                        }
                    }
            }else{
                dialog.invalidDialog("Invalid", "Enter email and password")
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
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_closed, 0)
        }
        editText.setSelection(editText.text.length) // keep cursor at end
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile(
            "^[A-Za-z0-9+-.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val pattern = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%=*?&])[A-Za-z\\d@\$!=%*?&]{8,}$")
        val matcher = pattern.matcher(password)
        return matcher.matches()
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