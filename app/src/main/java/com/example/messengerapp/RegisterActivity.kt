package com.example.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference

    private var firebaseUserID: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar =findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            val intent= Intent(this,WelcomeActivity::class.java)
            startActivity(intent)

            finish()
        }

        mAuth=FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            registerUser()
        }
    }
private fun registerUser(){

    val username:String=username_register.text.toString()
    val email:String=email_register.text.toString()
    val password:String=password_register.text.toString()


    if(username.isEmpty())
    {
      Toast.makeText(this,"Please fill in Username",Toast.LENGTH_LONG).show()
    }
    else if(email.isEmpty())
    {
        Toast.makeText(this,"Please fill in email",Toast.LENGTH_LONG).show()
    }
    else if(password.isEmpty())
    {
        Toast.makeText(this,"Please fill in password",Toast.LENGTH_LONG).show()
    }
    else
    {
       mAuth.createUserWithEmailAndPassword(email,password)
           .addOnCompleteListener {task->
               if (task.isSuccessful)
               {
            firebaseUserID=mAuth.currentUser!!.uid
                   refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                   val userHashMap=HashMap<String,Any>()

                   userHashMap["uid"]=firebaseUserID
                   userHashMap["username"]=username
                   userHashMap["profile"]="https://firebasestorage.googleapis.com/v0/b/messagingapp-c6350.appspot.com/o/profile.png?alt=media&token=9904438f-c1fe-43e6-8338-f8bdea6668e7"
                   userHashMap["cover"]="https://firebasestorage.googleapis.com/v0/b/messagingapp-c6350.appspot.com/o/cover.jpg?alt=media&token=763a636d-694e-4f8d-87d3-7459456fa82f"
                   userHashMap["status"]="offline"
                   userHashMap["search"]=username.toLowerCase()
                   userHashMap["facebook"]="https://m.facebook.com"
                   userHashMap["instagram"]="https://instagram.com"
                   userHashMap["website"]="https://google.com"

                   refUsers.updateChildren(userHashMap)
                       .addOnCompleteListener { task->

                           if(task.isSuccessful)
                           {
                               val intent= Intent(this,MainActivity::class.java)
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                               startActivity(intent)
                               finish()
                           }
                       }
               }
               else
               {

                   Toast.makeText(this,"Error Message: " +task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
               }

           }
    }
}

}
