package com.example.thedeath38.lista

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.thedeath38.firebase.Coche

import com.example.thedeath38.lista.dummy.DummyContent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*

import kotlinx.android.synthetic.main.item_list.*
import java.util.ArrayList

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false

    lateinit var coches:CocheDatos
    var arrayCoches:ArrayList<Coche> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("coche") //igual a la de la base de datos

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                println("data change")
                for (objj in dataSnapshot.children) {
                    val registro = objj.key
                    var coche = Coche()

                    for (dat in dataSnapshot.child(registro).children){
                        coche.matricula=registro
                        if (dat.key == "combustible"){
                            coche.combustible= dat.value.toString()
                        }
                        if (dat.key == "marca"){
                            coche.marca=dat.value.toString()
                        }
                        if (dat.key == "motor"){
                            coche.motor=dat.value.toString()
                        }
                    }
                    arrayCoches?.add(coche)
                }
                for(i in arrayCoches!!.iterator()){
                    println("matricula = "+i.matricula)
                    println("Motor = "+i.motor)
                    println("Combustible = "+i.combustible)
                    println("marca = "+i.marca)

                }
                coches=CocheDatos(arrayCoches)
                setupRecyclerView(item_list)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        myRef.addValueEventListener(menuListener)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            mTwoPane = true
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, coches.getCocheList(), mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ItemListActivity,
                                        private val mValues: List<Coche>,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as Coche
                if (mTwoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle()
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.matricula)
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.matricula)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mIdView.text = item.matricula //titulo list view

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
