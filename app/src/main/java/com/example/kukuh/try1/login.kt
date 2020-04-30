package com.example.kukuh.try1

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kukuh.try1.DashboardActivity
import com.example.kukuh.try1.login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser

class login : AppCompatActivity() {
    var Email: EditText? = null
    var Password: EditText? = null
    var LogInButton: Button? = null
    var RegisterButton: Button? = null
    var mAuth: FirebaseAuth? = null
    var mAuthListner: AuthStateListener? = null
    var mUser: FirebaseUser? = null
    var email: String? = null
    var password: String? = null
    var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogInButton = findViewById<View>(R.id.buttonLogin) as Button
        RegisterButton = findViewById<View>(R.id.buttonRegister) as Button
        Email = findViewById<View>(R.id.editEmail) as EditText
        Password = findViewById<View>(R.id.editPassword) as EditText
        dialog = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        mUser = FirebaseAuth.getInstance().currentUser
        mAuthListner = AuthStateListener {
            if (mUser != null) {
                val intent = Intent(this@login, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                Log.d(TAG, "AuthStateChanged:Logout")
            }
        }
        // LogInButton.setOnClickListener((View.OnClickListener) this);
        //RegisterButton.setOnClickListener((View.OnClickListener) this);
        //Adding click listener to log in button.
        LogInButton!!.setOnClickListener { // Calling EditText is empty or no method.
            userSign()
        }

        // Adding click listener to register button.
        RegisterButton!!.setOnClickListener { // Opening new user registration activity using intent on button click.
            val intent = Intent(this@login, Register::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth!!.removeAuthStateListener(mAuthListner!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListner != null) {
            mAuth!!.removeAuthStateListener(mAuthListner!!)
        }
    }

    override fun onBackPressed() {
        super@login.finish()
    }

    private fun userSign() {
        email = Email!!.text.toString().trim { it <= ' ' }
        password = Password!!.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@login, "Enter the correct Email", Toast.LENGTH_SHORT).show()
            return
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@login, "Enter the correct password", Toast.LENGTH_SHORT).show()
            return
        }
        dialog!!.setMessage("Loging in please wait...")
        dialog!!.isIndeterminate = true
        dialog!!.show()
        mAuth!!.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                dialog!!.dismiss()
                Toast.makeText(this@login, "Login not successfull", Toast.LENGTH_SHORT).show()
            } else {
                dialog!!.dismiss()
                checkIfEmailVerified()
            }
        }
    }

    //This function helps in verifying whether the email is verified or not.
    private fun checkIfEmailVerified() {
        val users = FirebaseAuth.getInstance().currentUser
        val emailVerified = users!!.isEmailVerified
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show()
            mAuth!!.signOut()
            finish()
        } else {
            Email!!.text.clear()
            Password!!.text.clear()
            val intent = Intent(this@login, DashboardActivity::class.java)

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(userEmail, email)
            startActivity(intent)
        }
    }

    companion object {
        const val userEmail = ""
        const val TAG = "LOGIN"
    }
}