package Ayuda.a3ayuda

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ubicacion.btn_Acceder
import kotlinx.android.synthetic.main.activity_ubicacion_g_p_s.*
import java.text.SimpleDateFormat
import java.util.*


class UbicacionGPSActivity : AppCompatActivity() {

    val REQUEST_CODE_LOCATION_PERMISSION=1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var contexto=this
    var latitud : Double =0.00
    var longitud : Double =0.00
    lateinit var direccion : TextView
    private lateinit var resultReceiver : ResultReceiver
    val db = FirebaseFirestore.getInstance()
    @RequiresApi(Build.VERSION_CODES.M) //GPS funciona apartir de android 19 creo :,D
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion_g_p_s)
        direccion = tv_direccion

        btn_Acceder.setOnClickListener {

            if (!direccion.text.isEmpty()&&latitud!=0.00&&longitud!=0.00) {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val sdf2 = SimpleDateFormat("hh:mm:ss dd/MM/yyyy")
                val fechaconhora= sdf2.format(Date())
                val acct = GoogleSignIn.getLastSignedInAccount(this)
                var bundle=intent.extras
                var idRef=db.collection("trabajos").document()
                var id=idRef.id
                var trabajo = Trabajo("abierto",currentDate, acct?.email!!,bundle?.getString("idServicio")!!,id,"geo:0,0?q=$latitud, $longitud",acct.givenName + " " + acct.familyName)
                idRef.set(trabajo)
                var intent = Intent(this, MensajeActivity::class.java)
                intent.putExtra("mensaje","EL TRABAJADOR HA SIDO NOTIFICADO")
                intent.putExtra("siguiente", Constantes.VENTANA_PERFIL_ADULTO)
                startActivity(intent)
            }else
            {
                val manager =contexto.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(this,"Para obtener la ubicación debe activar el GPS.",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                else {
                    Toast.makeText(this,"Ubicación está vacía. Vuelva a intentarlo.",Toast.LENGTH_SHORT).show()
                    iniciarChecarUbicacion()
                }
            }
        }

        btn_Cancelar.setOnClickListener{
            var intent= Intent(this, ListaPersonalActivity::class.java)
            startActivity(intent)
        }

        iniciarChecarUbicacion()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        iniciarChecarUbicacion()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun iniciarChecarUbicacion()
    {
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
        solicitudUbicacion.interval = 10000
        solicitudUbicacion.fastestInterval = 3000
        solicitudUbicacion.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                LocationServices.getFusedLocationProviderClient(contexto).removeLocationUpdates(this)
                if(locationResult!=null && locationResult.locations.size>0){
                    var latestLocationIndex = locationResult.locations.size-1
                    latitud= locationResult.locations.get(latestLocationIndex).latitude
                    longitud =locationResult.locations.get(latestLocationIndex).longitude
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
