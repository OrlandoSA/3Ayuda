package Ayuda.a3ayuda

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_lista_personal.*
import kotlinx.android.synthetic.main.personal.view.*

class ListaPersonalActivity : AppCompatActivity() {

    var listaPersonal=ArrayList<Personal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_personal)
        cargarPersonal()
        var adaptador=AdaptadorPersonal(this,listaPersonal)
        listview.adapter=adaptador
    }

    fun cargarPersonal(){
        listaPersonal.add(Personal("Hitomi","Flores",34,"Cuidados",R.drawable.personal3,"6444637485"))
        listaPersonal.add(Personal("Celina","Acosta",42,"Pintura",R.drawable.personal6,"6442857496"))
        listaPersonal.add(Personal("Salvador","Villanueva",26,"Fontanero",R.drawable.personal2,"6441748596"))
        listaPersonal.add(Personal("Alfredo","Pérez",41,"Arreglos generales",R.drawable.personal4,"6441235478"))
        listaPersonal.add(Personal("Rosalía","López",50,"Empleada domestica",R.drawable.personal1,"6441238574"))
        listaPersonal.add(Personal("Rocío","Santos",29,"Cocinera",R.drawable.personal5,"6441189652"))
    }

    private class AdaptadorPersonal:BaseAdapter{
        var personal=ArrayList<Personal>()
        var contexto:Context?=null

        constructor(contexto:Context,personal:ArrayList<Personal>){
            this.contexto=contexto
            this.personal=personal
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var persona=personal[position]
            var inflador= LayoutInflater.from(contexto)
            var vista=inflador.inflate(R.layout.personal,null)

            vista.tv_Nombre.setText(persona.nombre+" "+persona.apellido)
            vista.tv_Edad.setText(persona.edad.toString())
            vista.tv_Servicio.setText(persona.servicio)
            vista.iv_imagen.setImageResource(persona.imagen)

            vista.setOnClickListener{
                var intent = Intent(contexto, PerfilPersonalActivity::class.java)
                intent.putExtra("imagen", persona.imagen)
                intent.putExtra("nombre", persona.nombre+" "+persona.apellido)
                intent.putExtra("edad", persona.edad)
                intent.putExtra("telefono",persona.telefono)
                contexto!!.startActivity(intent)
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
