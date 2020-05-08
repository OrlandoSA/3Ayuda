package Ayuda.a3ayuda

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_ubicacion.*

class UbicacionGPSActivity : AppCompatActivity() {

    val REQUEST_CODE_LOCATION_PERMISSION=1;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var contexto=this
    lateinit var direccion : TextView
    private lateinit var resultReceiver : ResultReceiver

    @RequiresApi(Build.VERSION_CODES.M) //GPS funciona apartir de android 19 creo :,D
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion_g_p_s)

        btn_Acceder.setOnClickListener {
            var intent = Intent(this, MensajeActivity::class.java)
            intent.putExtra("mensaje","UBICACIÓN ENVIADA")
            intent.putExtra("siguiente", Constantes.VENTANA_BUZON_SUGERENCIAS)
            startActivity(intent)
        }

        resultReceiver= RecibidorDeDireccionResultante(Handler())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        direccion=findViewById(R.id.tv_direccion)

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //si no hay permisos se piden
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
        }else{
            //si si tiene permisos se procede a guardar
            obtenerUbicacionActual()
        }


    }

    fun obtenerUbicacionActual(){
        var solicitudUbicacion: LocationRequest = LocationRequest()
        solicitudUbicacion.setInterval(10000)
        solicitudUbicacion.setFastestInterval(3000)
        solicitudUbicacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                LocationServices.getFusedLocationProviderClient(contexto).removeLocationUpdates(this)
                if(locationResult!=null && locationResult.locations.size>0){
                    var latestLocationIndex = locationResult.locations.size-1
                    var latitud= locationResult.locations.get(latestLocationIndex).latitude
                    var longitud =locationResult.locations.get(latestLocationIndex).longitude
                    //colonia.text="Latitud: $latitud y Longitud : $longitud"

                   var loc: Location = Location("providerNA")
                   loc.latitude=latitud
                   loc.longitude=longitud
                    obtenerFormatoDesdeLatLong(loc)
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
            }
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(solicitudUbicacion, locationCallback, Looper.getMainLooper())
    }

    fun obtenerFormatoDesdeLatLong(location : Location){
        var intent = Intent(contexto, ObtenerDatosDireccion::class.java)
        intent.putExtra(Constantes.RECEIVER,resultReceiver)
        intent.putExtra(Constantes.LOCATION_DATA_EXTRA,location)
        startService(intent)
    }

    inner class RecibidorDeDireccionResultante(handler: Handler?) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)

            if(resultCode==Constantes.SUCCESS_RESULT){
                direccion.text= resultData?.getString(Constantes.RESULT_DATA_KEY)
            }else{
                Toast.makeText(contexto, resultData?.getString(Constantes.RESULT_DATA_KEY),Toast.LENGTH_SHORT).show()
            }
        }
    }
}
