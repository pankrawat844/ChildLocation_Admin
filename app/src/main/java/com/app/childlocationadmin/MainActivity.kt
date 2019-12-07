package com.app.childlocationadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.childlocationadmin.utils.RecyclerItemClickListener
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private var list:ArrayList<com.app.childlocationadmin.Location> =ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseReference= FirebaseDatabase.getInstance().getReference("location")
        val usersRef = databaseReference!!.child("users")
        databaseReference.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    Log.e("result",postSnapshot.toString())
                    val item:Location=postSnapshot.getValue(Location::class.java)!!
                    list.add(item)
                }
                initRecylerview(list.toListItem())
            }

        })

        recycler!!.addOnItemTouchListener(RecyclerItemClickListener(this!!,object :RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int) {
                Intent(this@MainActivity,MapsActivity::class.java).also {
                    it.putExtra("name","test")
                    startActivity(it)
                }
            }

        }))
    }
    private fun List<com.app.childlocationadmin.Location>.toListItem():List<ItemRecylerview>
    {
        return this.map {
            ItemRecylerview(it)
        }
    }
    private fun initRecylerview(toListItem: List<ItemRecylerview>) {
        val grpadapter= GroupAdapter<ViewHolder>().apply {
            addAll(toListItem)
        }
        recycler.layoutManager= LinearLayoutManager(this)
        recycler.adapter=grpadapter
    }
}
