package com.example.thedeath38.lista

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thedeath38.firebase.Coche
import com.example.thedeath38.lista.dummy.DummyContent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var mItem: Coche? = null
    var arrayCoches:ArrayList<Coche> = ArrayList()
    lateinit var coches:CocheDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                if (arguments.containsKey(ARG_ITEM_ID)) {
                    // Load the dummy content specified by the fragment
                    // arguments. In a real-world scenario, use a Loader
                    // to load content from a content provider.
                    println(arguments.getString(ARG_ITEM_ID))
                    mItem = coches.getIdCoche(arguments.getString(ARG_ITEM_ID))
                    mItem?.let {
                        activity.toolbar_layout?.title = it.matricula
                        activity.et_marca.setText(it.marca)
                        activity.etMotor.setText(it.motor)
                        activity.etCombustible.setText(it.combustible)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        myRef.addValueEventListener(menuListener)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
        mItem?.let {
            /*rootView.et_marca.setText(it.marca)
            rootView.etMotor.setText(it.motor)
            rootView.etCombustible.setText(it.combustible)*/
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
