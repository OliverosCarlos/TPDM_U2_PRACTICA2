package mx.edu.ittepic.myhomwor

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

class FormTarea : AppCompatActivity() {
    var btnCrearTarea: Button? = null
    var txtNombreTarea: EditText? = null
    var txtDescripcion: EditText? = null
    var txtFechaActual: EditText ?= null

    var btnShowVC : Button ?= null
    var btnShowVP : Button ?= null

    var viewCalendar : LinearLayout?= null
    var viewParticipantes : LinearLayout?= null

    var VP = false
    var VC = false

    var db = DataBase(this, "dbtareas", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_tarea)

        btnCrearTarea = findViewById(R.id.btnCrearTarea)
        txtNombreTarea = findViewById(R.id.txtNombreTarea)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        txtFechaActual = findViewById(R.id.txtFechaActual)

        btnShowVC = findViewById(R.id.showVC)
        btnShowVP = findViewById(R.id.showVP)

        viewCalendar = findViewById(R.id.viewCalendar)
        viewParticipantes = findViewById(R.id.viewParticipantes)

        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy/MM/dd HH:mm:ss")
        txtFechaActual?.setText(dateInString)

        btnCrearTarea?.setOnClickListener {
            crearTarea()
        }

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
    }

    fun crearTarea() {
        try {

            var result = db.writableDatabase

            var query =
                "INSERT INTO tareas(nombre_tarea,descripcion_tarea) VALUES('NOM_TAREA','DESC_TAREA');"

            query = query.replace("NOM_TAREA", txtNombreTarea?.text.toString())
            query = query.replace("DESC_TAREA", txtDescripcion?.text.toString())

            result.execSQL(query)
            result.close()
            Toast.makeText(this,"Tarea creada con éxito", Toast.LENGTH_LONG).show()
        }catch (err: SQLiteException){
            mensaje("Error",""+err)
        }
    }

    fun mensaje(t: String, m: String) {
        AlertDialog.Builder(this).setTitle(t).setMessage(m)
            .setPositiveButton("OK") { d, w -> }
            .show()
    }


    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}
