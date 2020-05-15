package Ayuda.a3ayuda

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_pendientes.*
import kotlinx.android.synthetic.main.trabajos.view.*

class PendientesActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    var listaTrabajos = ArrayList<Trabajo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendientes)

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        agregarListenerTrabajos()
    }

    private fun actualizarLista() {
        val adaptador = AdaptadorTrabajos(this, listaTrabajos)
        lv_trabajos.adapter = adaptador
    }

    private fun agregarListenerTrabajos() {
        db.collection("trabajos")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Error", "Listen failed.", e)
                    return@addSnapshotListener
                }
                for (doc in value!!) {
                    val trabajo = doc.toObject(Trabajo::class.java)
                    if (trabajo.idCliente.equals(GoogleSignIn.getLastSignedInAccount(this)?.email)&&trabajo.estado.equals("abierto")) {
                        if (!listaTrabajos.contains(trabajo)) {
                            listaTrabajos.add(trabajo)
                        }
                    }
                }
                actualizarLista()
            }
    }

    private class AdaptadorTrabajos : BaseAdapter {
        var trabajos: List<Trabajo>? = null
        var contexto: Context? = null

        constructor(contexto: Context, trabajos: ArrayList<Trabajo>) {
            this.contexto = contexto
            this.trabajos = trabajos.sortedWith(compareByDescending { it.fechaCreacion })
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val trabajo = trabajos!![position]
            val inflador = LayoutInflater.from(contexto)
            val vista = inflador.inflate(R.layout.trabajos, null)

            regresaPersonal(trabajo.idEmpleado, vista)

            vista.tv_fecha.text = trabajo.fechaCreacion


            vista.setOnClickListener {
                val intent = Intent(contexto!!, ChatActivity::class.java)
                intent.putExtra("idTrabajo", trabajo.idTrabajo)
                intent.putExtra("idEmpleado", trabajo.idEmpleado)
                intent.putExtra("idCliente", trabajo.idCliente)
                intent.putExtra("servicio", vista.tv_servicio.text)
                intent.putExtra("fecha", trabajo.fechaCreacion)
                intent.putExtra("nombre", vista.tv_nombre.text)
                contexto!!.startActivity(intent)
            }
            return vista
        }

        fun regresaPersonal(id:String, vista: View){

            var db= FirebaseFirestore.getInstance()
            db.collection("perfiles")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w("Error", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    for (doc in value!!) {
                         if (doc.id.equals(id)) {
                             vista.tv_servicio.text = doc.data.get("servicios").toString()
                             vista.tv_nombre.text = doc.data.get("nombre").toString()
                        }
                    }
                }
        }

        override fun getItem(position: Int): Any {
            return trabajos!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return trabajos!!.size
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}
