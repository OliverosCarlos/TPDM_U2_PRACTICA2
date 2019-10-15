package mx.edu.ittepic.myhomwor

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_form_list.*
import kotlinx.android.synthetic.main.activity_form_tarea.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class FormList : AppCompatActivity() {
    //FORMULARIO LISTA
    var btnCrearLista: Button? = null
    var txtNombreLista: EditText? = null
    var txtDescripcionLista: EditText? = null
    var txtFechaActual: TextView?= null

    //FORMULARIO TAREA
    var btnAgregarTarea : Button ?= null
    var txtNombreTarea : EditText ?= null
    var txtDescripcionTarea : EditText ?= null

    var btnShowVC : Button ?= null
    var btnShowVP : Button ?= null
    var btnShowVT : Button ?= null

    var viewCalendar : LinearLayout ?= null
    var viewParticipantes : LinearLayout ?= null
    var viewTareas : LinearLayout ?= null
    var viewTareasExistentes : LinearLayout ?= null

    var calendario : CalendarView ?= null

    var VP = false
    var VC = false
    var VT = false

    var listExist = false
    var id_lista = 0

    var db = DataBase(this, "dbtareas", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_list)


        //ASOCIACION FORMULARIO LISTA
        btnCrearLista = findViewById(R.id.btnCrearLista)
        txtNombreLista = findViewById(R.id.txtNombreLista)
        txtDescripcionLista = findViewById(R.id.txtDescripcionLista)
        txtFechaActual = findViewById(R.id.txtFechaActual)

        //ASOCIACION FORMULARIO TAREA
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea)
        txtNombreTarea = findViewById(R.id.txtNombreTarea)
        txtDescripcionTarea = findViewById(R.id.txtDescripcionTarea)

        //AS BOTONES QUE MUESTRAS LOS RESPECTIVOS LINEAR LAYOUT
        btnShowVC = findViewById(R.id.showVC)
        btnShowVP = findViewById(R.id.showVP)
        btnShowVT = findViewById(R.id.showVT)

        //AS LINEAR LAYOUT QUE SE OCULTA
        viewCalendar = findViewById(R.id.viewCalendar)
        viewParticipantes = findViewById(R.id.viewParticipantes)
        viewTareas = findViewById(R.id.viewTareas)
        viewTareasExistentes = findViewById(R.id.viewTareasExistentes)

        calendario = findViewById(R.id.calendario)


        btnShowVC?.setOnClickListener {
            if (!VC) {
                viewCalendar?.visibility = View.VISIBLE
                VC = true

            }else{
                viewCalendar?.visibility = View.GONE
                VC = false
            }
        }
        btnShowVP?.setOnClickListener {
            if (!VP) {
                viewParticipantes?.visibility = View.VISIBLE
                VP = true

            }else{
                viewParticipantes?.visibility = View.GONE
                VP = false
            }
        }
        btnShowVT?.setOnClickListener {
            if (!VT) {
                viewTareas?.visibility = View.VISIBLE
                VT = true
                if(!listExist){
                    crearLista()
                    listExist = true
                    btnCrearLista?.setText("REGRESAR")
                }

            }else{
                viewTareas?.visibility = View.GONE
                VT = false
            }
        }

        btnCrearLista?.setOnClickListener {
            if(listExist){
                finish()
            }else
            crearLista() }
        btnAgregarTarea?.setOnClickListener {
            getUltimoIdLista()
            crearTarea() }

        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy/MM/dd HH:mm:ss")
        txtFechaActual?.setText(dateInString)

        }

    fun crearLista() {
        try {

            var result = db.writableDatabase

            var query =
                "INSERT INTO listas(nombre_lista,descripcion_lista,fecha_creacion_lista,fecha_finalizacion_lista) VALUES('NOM_LIST','DESC_LIST','FECHAI','FECHAF');"

            query = query.replace("NOM_LIST", txtNombreLista?.text.toString())
            query = query.replace("DESC_LIST", txtDescripcionLista?.text.toString())
            query = query.replace("FECHAI", txtFechaActual?.text.toString())
            query = query.replace("FECHAF", ""+calendario?.date)

            result.execSQL(query)
            result.close()
            Toast.makeText(this,"Lista creada con éxito", Toast.LENGTH_LONG).show()
        }catch (err: SQLiteException){
            mensaje("Error",""+err)
        }
    }

    fun mensaje(t: String, m: String) {
        AlertDialog.Builder(this).setTitle(t).setMessage(m)
            .setPositiveButton("OK") { d, w -> }
            .show()
    }

    fun crearTarea() {
        try {

            var result = db.writableDatabase

            var query =
                "INSERT INTO tareas(nombre_tarea,descripcion_tarea,fk_id_lista) VALUES('NOM_TAREA','DESC_TAREA',IDLISTA);"
            Toast.makeText(this,"ID lista: "+id_lista, Toast.LENGTH_LONG).show()
            query = query.replace("NOM_TAREA", txtNombreTarea?.text.toString())
            query = query.replace("DESC_TAREA", txtDescripcionTarea?.text.toString())
            query = query.replace("IDLISTA",""+id_lista)

            result.execSQL(query)
            result.close()
            Toast.makeText(this,"Tarea creada con éxito", Toast.LENGTH_LONG).show()
            showTareas()
        }catch (err: SQLiteException){
            mensaje("Error",""+err)
        }
    }

    fun getUltimoIdLista(): Int{

        try{
            var result = db.readableDatabase
            var query = "SELECT id_lista FROM listas"
            var cursor = result.rawQuery(query,null)
            if(result !=null){
                cursor.moveToLast()
                    id_lista = cursor.getInt(0)
                    Toast.makeText(this,"Ultimo id"+id_lista, Toast.LENGTH_LONG).show()

            }
        }catch(err:SQLiteException){
            mensaje("Error",""+err)
        }
        return id_lista
    }

    fun showTareas(){
        try{
            viewTareasExistentes?.removeAllViews()
            var result = db.readableDatabase
            var query = "SELECT * FROM tareas WHERE fk_id_lista="+getUltimoIdLista()
            var cursor = result.rawQuery(query,null)
            if(result !=null){
                while(cursor.moveToNext()) {
                    var layElemenTar =
                        newLinearLayout(newLabel(cursor.getString(0)),newLabel(cursor.getString(1)),newLabel(cursor.getString(2)),newLabel(cursor.getString(6)), newButton("EDIT"),newButton("Eliminar"))
                    viewTareasExistentes?.addView(layElemenTar)
                }
            }
        }catch(err:SQLiteException){
            mensaje("Error",""+err)
        }
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun newLabel(nomTarea: String) : TextView{

        var etiqueta = TextView(this)
        etiqueta.setText(nomTarea)

        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(10,10,10,10)
        etiqueta.layoutParams = param
        return etiqueta
    }
    fun newButton(etiquetaBoton: String): Button{
        var boton = Button(this)
        boton?.setText(etiquetaBoton)

        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(10,10,10,10)
        param.weight
        boton.layoutParams = param
        return boton
    }
    fun newLinearLayout(lblId: TextView,lblNom :TextView,lblDesc:TextView,lblFkId:TextView,btnEdit:Button,btnDelete:Button): LinearLayout{
        var linear = LinearLayout(this)
        linear?.orientation = LinearLayout.HORIZONTAL
        linear.addView(lblId)
        linear.addView(lblNom)
        linear.addView(lblDesc)
        linear.addView(lblFkId)
        linear.addView(btnEdit)
        linear.addView(btnDelete)
        return linear
    }

}
