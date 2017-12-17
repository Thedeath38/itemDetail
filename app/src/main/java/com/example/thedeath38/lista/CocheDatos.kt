package com.example.thedeath38.lista

import com.example.thedeath38.firebase.Coche
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Thedeath38 on 01/12/2017.
 */
class CocheDatos {
    private  var arrayCoches: MutableList<Coche> = ArrayList()

    constructor(arrayCoches: MutableList<Coche>) {
        this.arrayCoches = arrayCoches
    }


    public  fun getCocheList(): MutableList<Coche> {
        return arrayCoches
    }
    public fun getIdCoche(id:String): Coche{
        for (obbj in arrayCoches) {
            if (obbj.matricula == id) {
                return obbj
            }
        }
        return arrayCoches.get(0)
    }
}