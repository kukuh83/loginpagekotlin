package com.example.kukuh.try1

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kukuh.try1.login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class Register : AppCompatActivity(), View.OnClickListener {
    var name: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var mRegisterbtn: Button? = null
    var mLoginPageBack: TextView? = null
    var mAuth: FirebaseAuth? = null
    var mdatabase: DatabaseReference? = null
    var displayName: String? = null
    var userEmail: String? = null
    var Password: String? = null
    var mDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        name = findViewById<View>(R.id.editName) as EditText
        email = findViewById<View>(R.id.editEmail) as EditText
        password = findViewById<View>(R.id.editPassword) as EditText
        mRegisterbtn = findViewById<View>(R.id.buttonRegister) as Button
        mLoginPageBack = findViewById<View>(R.id.buttonLogin) as TextView
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance()
        mRegisterbtn!!.setOnClickListener(this)
        mLoginPageBack!!.setOnClickListener(this)
        mDialog = ProgressDialog(this)
        mdatabase = FirebaseDatabase.getInstance().reference.child("Users")
    }

    override fun onClick(v: View) {
        if (v === mRegisterbtn) {
            UserRegister()
        } else if (v === mLoginPageBack) {
            startActivity(Intent(this@Register, login::class.java))
        }
    }

    private fun UserRegister() {
        displayName = name!!.text.toString().trim { it <= ' ' }
        userEmail = email!!.text.toString().trim { it <= ' ' }
        Password = password!!.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(displayName)) {
            Toast.makeText(this@Register, "Enter Name", Toast.LENGTH_SHORT).show()
            return
        } else if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this@Register, "Enter Email", Toast.LENGTH_SHORT).show()
            return
        } else if (TextUtils.isEmpty(Password)) {
            Toast.makeText(this@Register, "Enter Password", Toast.LENGTH_SHORT).show()
            return
        } else if (Password!!.length < 6) {
            Toast.makeText(this@Register, "Passwor must be greater then 6 digit", Toast.LENGTH_SHORT).show()
            return
        }
        mDialog!!.setMessage("Creating User please wait...")
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
        mAuth!!.createUserWithEmailAndPassword(userEmail!!, Password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendEmailVerification()
                mDialog!!.dismiss()
                OnAuth(task.result.user)
                mAuth!!.signOut()
            } else {
                Toast.makeText(this@Register, "error on creating user", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private fun sendEmailVerification() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@Register, "Check your Email for verification", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
            }
        }
    }

    private fun OnAuth(user: FirebaseUser) {
        createAnewUser(user.uid)
    }

    private fun createAnewUser(uid: String) {
        val user = BuildNewuser()
        mdatabase!!.child(uid).setValue(user)
    }

    private fun BuildNewuser(): User {
        return User(
                displayName,
                userEmail,
                Date().time
        )
    }

}