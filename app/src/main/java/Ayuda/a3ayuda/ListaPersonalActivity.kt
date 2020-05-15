package Ayuda.a3ayuda

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_lista_personal.*
import kotlinx.android.synthetic.main.personal.view.*


class ListaPersonalActivity : AppCompatActivity() {

    var listaPersonal=ArrayList<Personal>()
    var db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_personal)

        cargarPersonal()
    }

    fun actualizarListaPersonal(){
        var adaptador=AdaptadorPersonal(this,listaPersonal)
        listview.adapter=adaptador
    }


    fun cargarPersonal(){
        db.collection("perfiles")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Error", "Listen failed.", e)
                    return@addSnapshotListener
                }
                for (doc in value!!) {
                    val persona= Personal(doc.id, Integer.parseInt(doc.data.get("edad").toString()), doc.data.get("nombre").toString(), doc.data.get("servicios").toString(), "perfiles/${doc.id}.jpg")
                    if (!listaPersonal.contains(persona)) {
                        listaPersonal.add(persona)
                    }
                }
                actualizarListaPersonal()
            }
    }

    private class AdaptadorPersonal:BaseAdapter{
        var personal=ArrayList<Personal>()
        var contexto : Context

        constructor(contexto:Context,personal:ArrayList<Personal>){
            this.contexto=contexto
            this.personal=personal

        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var persona=personal[position]
            var inflador= LayoutInflater.from(contexto)
            var vista=inflador.inflate(R.layout.personal,null)

            vista.tv_Nombre.text = persona.nombre
            vista.tv_Edad.text = persona.edad.toString()
            vista.tv_Servicio.text = persona.servicio

            if(persona.imagen!="") {
                var algo=FirebaseStorage.getInstance().reference
                val equis=algo.child(persona.imagen)

                equis.downloadUrl.addOnSuccessListener {Uri->
                    val imageURL = Uri.toString()
                    Glide.with(contexto)
                        .load(imageURL)
                        .into(vista.iv_imagen)
                }
            }
            else
                Picasso.get().load("https://www.ecured.cu/images/thumb/7/7b/Thalia.jpg/260px-Thalia.jpg").into(vista.iv_imagen)

            vista.setOnClickListener{
                var intent = Intent(contexto, PerfilPersonalActivity::class.java)
                intent.putExtra("imagen", persona.imagen)
                intent.putExtra("nombre", persona.nombre)
                intent.putExtra("edad", persona.edad)
                intent.putExtra("idServicio", persona.id)
                contexto.startActivity(intent)
            }

            return vista
        }


        override fun getItem(position: Int): Any {
            return personal[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return personal.size
        }
    }
}
