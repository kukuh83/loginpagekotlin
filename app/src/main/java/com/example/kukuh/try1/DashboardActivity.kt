package com.example.kukuh.try1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser

class DashboardActivity : AppCompatActivity() {
    var EmailHolder: String? = null
    var Email: TextView? = null
    var LogOUT: Button? = null
    var mAuth: FirebaseAuth? = null
    var mAuthListner: AuthStateListener? = null
    var mUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        Email = findViewById<View>(R.id.textView1) as TextView
        LogOUT = findViewById<View>(R.id.button1) as Button
        val intent = intent

        // Receiving User Email Send By MainActivity.
        EmailHolder = intent.getStringExtra(login.userEmail)

        // Setting up received email to TextView.
        Email!!.text = Email!!.text.toString() + EmailHolder

        // Adding click listener to Log Out button.
        LogOUT!!.setOnClickListener { //Finishing current DashBoard activity on button click.
            finish()
            Toast.makeText(this@DashboardActivity, "Log Out Successfull", Toast.LENGTH_LONG).show()
            //Intent intent=new Intent(DashboardActivity.this,login.class);
            //startActivity(intent);
            /*if (v.getId() == R.id.button1) {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    startActivity(new Intent(DashboardActivity.this, login.class));
                                    finish();
                                }
                            });
                }*/
        }
    }

    companion object {
        //@SuppressLint("SetTextI18n")
        const val TAG = "LOGIN"
    }
}