package mx.edu.ittepic.myhomwor

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var btnSH : Button ?= null
    var btnSL : Button ?= null

    var btnNuevaTarea : Button ?= null
    var btnNuevaLista : Button ?= null

    var viewTareas : ScrollView ?= null
    var viewList : ScrollView ?= null

    var layViewTareas : LinearLayout ?= null
    var layViewListas : LinearLayout ?= null

    var tareasVisible :Boolean = false
    var listaVisible :Boolean = false

    var id_lista = 0
    var list_IdListas: MutableList<Int> = mutableListOf()

    var database = DataBase(this, "dbtareas",null,1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSH = findViewById(R.id.ShowHW)
        viewTareas = findViewById(R.id.viewTareas)
        btnSL = findViewById(R.id.ShowSL)
        viewList = findViewById(R.id.viewList)

        btnNuevaTarea = findViewById(R.id.btnNuevaTarea)
        btnNuevaLista = findViewById(R.id.btnNuevaLista)

        layViewTareas = findViewById(R.id.LayoutViewTareas)
        layViewListas = findViewById(R.id.LayoutViewListas)

        btnNuevaTarea?.setOnClickListener { nuevaTarea() }
        btnNuevaLista?.setOnClickListener { nuevaLista() }



        showListas()

        btnSH?.setOnClickListener {
            if (!tareasVisible) {
                viewTareas?.visibility = View.VISIBLE
                tareasVisible = true

            }else{
                viewTareas?.visibility = View.GONE
                tareasVisible = false
            }
        }

        btnSL?.setOnClickListener {
            if(!listaVisible)
            {
               viewList?.visibility = View.VISIBLE
                listaVisible = true
            }
            else{
                viewList?.visibility = View.GONE
                listaVisible = false
            }

        }


    }

    fun nuevaTarea(){
        var windowFormaTarea = Intent(this,FormTarea::class.java)
        startActivity(windowFormaTarea)
    }

    fun nuevaLista(){
        var windowFormaLista = Intent(this,FormList::class.java)
        startActivity(windowFormaLista)
    }

    fun showTareas(id: Int){
        try{
            LayoutViewTareas?.removeAllViews()
            var result = database.readableDatabase
            var query = "SELECT * FROM tareas WHERE fk_id_lista="+id
            var cursor = result.rawQuery(query,null)
            if(result !=null){
                while(cursor.moveToNext()) {
                    list_IdListas.add(cursor.getInt(0))
                    var layElemenTar =
                        newLinearLayout(newLabel(cursor.getString(1)),newLabel(cursor.getString(2)), newButton("EDIT"),newButton("Eliminar"))
                    LayoutViewTareas?.addView(layElemenTar)

                }
            }
        }catch(err:SQLiteException){
            mensaje("Error",""+err)
        }
    }

    fun showListas(){
        try{
            var result = database.readableDatabase
            var query = "SELECT * FROM listas"
            var cursor = result.rawQuery(query,null)
            if(result !=null){
                while(cursor.moveToNext()) {
                    var layElemenList =
                        newLinearLayoutList(newLabel(cursor.getString(0)),newLabel(cursor.getString(1)),newLabel(cursor.getString(3)),newLabel(cursor.getString(4)), newButton("EDIT"),newButton("Eliminar"),cursor.getInt(0))
                    LayoutViewListas.addView(layElemenList)
                }
            }
        }catch(err:SQLiteException){
            mensaje("Error",""+err)
        }
    }

    fun mensaje(t: String, m: String) {
        AlertDialog.Builder(this).setTitle(t).setMessage(m)
            .setPositiveButton("OK") { d, w -> }
            .show()
    }



    //-----------------------------   METHOD TO CREATE ELEMENTS --------------------------------------------------------
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
    fun newLinearLayout(lblNom :TextView,lblDesc:TextView,btnEdit:Button,btnDelete:Button): LinearLayout{
        var linear = LinearLayout(this)
        var check = CheckBox(this)
        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(10,10,10,10)
        check.layoutParams = param
        linear?.orientation = LinearLayout.HORIZONTAL
        linear.addView(lblNom)
        linear.addView(lblDesc)
        linear.addView(check)
        linear.addView(btnEdit)
        linear.addView(btnDelete)
        return linear
    }

    fun newLinearLayoutList(lblId: TextView,lblNom :TextView,lblFechaI: TextView,lblFechaF :TextView,btnEdit:Button,btnDelete:Button,id:Int): LinearLayout{
        var linear = LinearLayout(this)
        linear.id = id
        linear.orientation = LinearLayout.HORIZONTAL
        linear.addView(lblId)
        linear.addView(lblNom)
        linear.addView(lblFechaI)
        linear.addView(lblFechaF)
        linear.addView(btnEdit)
        linear.addView(btnDelete)
        linear.setOnClickListener {
            showTareas(it.id)
        }
        return linear
    }
}
