package com.example.web_call_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.reflect.Array

class MainActivity : AppCompatActivity() {

    private lateinit var userRecylerView:RecyclerView

    private lateinit var userList: ArrayList<User>
    private lateinit var adaptor: userAdaptor
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth= FirebaseAuth.getInstance()
        mDbRef=FirebaseDatabase.getInstance().getReference()

        userList=ArrayList()
        adaptor= userAdaptor(this, userList)

        userRecylerView=findViewById(R.id.userRecylerView)
        userRecylerView.layoutManager=LinearLayoutManager(this)
        userRecylerView.adapter=adaptor
        mDbRef.child("user").addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser= postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid!= currentUser?.uid){
                        userList.add(currentUser!!)

                    }

                }
                adaptor.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logout){
            mAuth.signOut()
            val intent=Intent(this@MainActivity,Login::class.java)

            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}
