package Ayuda.a3ayuda

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import android.util.Log
import java.util.*


class ObtenerDatosDireccion : IntentService("ObtenerDatosDireccion") {

    lateinit var resultReceiver: ResultReceiver

    override fun onHandleIntent(intent: Intent?) {

        if(intent!=null){
            var mensajeError: String = ""
            resultReceiver=intent.getParcelableExtra(Constantes.RECEIVER)
            var locacion : Location = intent.getParcelableExtra(Constantes.LOCATION_DATA_EXTRA)
            if(locacion==null){
                return
            }
            var geocoder : Geocoder = Geocoder(this, Locale.getDefault())
            lateinit var direcciones : List<Address>
            try {
                direcciones=geocoder.getFromLocation(locacion.latitude,locacion.longitude,1)
            }catch (e: Exception){
                mensajeError=e.localizedMessage
            }

            if(direcciones==null || direcciones.isEmpty()){
                regresarResultado(Constantes.FAILURE_RESULT,mensajeError)
            }else{
                var direccion : Address = direcciones.get(0)
                var direccionEnPartes : ArrayList<String> = arrayListOf<String>()

                for (x in 0..direccion.maxAddressLineIndex) {
                    direccionEnPartes.add(direccion.getAddressLine(x))
                }
                //var secuenciachar: CharSequence = "\n"
                regresarResultado(Constantes.SUCCESS_RESULT, direccionEnPartes.toString())
            }
        }
    }

    fun regresarResultado(codigoResultado: Int, direccionMensaje: String){
        var nuevoBundle: Bundle = Bundle()
        nuevoBundle.putString(Constantes.RESULT_DATA_KEY,direccionMensaje)
        resultReceiver.send(codigoResultado, nuevoBundle)
    }

    //fun regresarResultadoArray(codigoResultado: Int, direccion: ArrayList<String>){
     //   var nuevoBundle: Bundle = Bundle()
     //   nuevoBundle.putStringArrayList(Constantes.RESULT_DATA_KEY,direccion)
     //   resultReceiver.send(codigoResultado, nuevoBundle)
    //}

}