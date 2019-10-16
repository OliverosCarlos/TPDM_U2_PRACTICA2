package mx.edu.ittepic.tpdm_u2_practica2

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import mx.edu.ittepic.tpdm_u2_practica2.BaseDatos

class DetalleCompra : AppCompatActivity() {

    var idDetalle: EditText? = null
    var buscar: ImageButton? = null
    var idAlmacen: EditText? = null
    var cantidad: EditText? = null
    var precio: EditText? = null
    var idCompra: EditText? = null
    var insertar: Button? = null
    var actualizar: Button? = null
    var eliminar: Button? = null
    var etiqueta: TextView? = null
    var regresar: Button? = null
    var basedatos = BaseDatos(this, "practica2", null, 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_compra)

        idDetalle = findViewById(R.id.idDetalle)
        buscar = findViewById(R.id.buscarDetalle)
        idAlmacen = findViewById(R.id.idAlmacenDetalle)
        cantidad = findViewById(R.id.cantidadDetalle)
        precio = findViewById(R.id.precioDetalle)
        idCompra = findViewById(R.id.idCompraDetalle)
        insertar = findViewById(R.id.insertarDetalle)
        actualizar = findViewById(R.id.actualizarDetalle)
        eliminar = findViewById(R.id.eliminarDetalle)
        etiqueta = findViewById(R.id.etiquetaDetalle)
        regresar = findViewById(R.id.regresarDetalle)

        buscarDetalles()

        buscar?.setOnClickListener {
            if(idDetalle?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa id del detalle de compra.")
            }else{
                buscarId(idDetalle?.text.toString())
            }
        }
        insertar?.setOnClickListener {
            if (idAlmacen?.text.toString().isEmpty() || cantidad?.text.toString().isEmpty() || precio?.text.toString().isEmpty() || idCompra?.text.toString().isEmpty()) {
                mensaje("ERROR", "Por favor, llena todos los campos.")
            } else {
                insertar(idAlmacen?.text.toString(), cantidad?.text.toString(), precio?.text.toString(), idCompra?.text.toString())
            }
        }
        actualizar?.setOnClickListener {
            if(idAlmacen?.text.toString().isEmpty() || cantidad?.text.toString().isEmpty() || precio?.text.toString().isEmpty() || idCompra?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                actualizar(idDetalle?.text.toString())
            }
        }
        eliminar?.setOnClickListener {
            if(idDetalle?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el id del detalle de compra.")
            }else{
                AlertDialog.Builder(this).setTitle("ADVERTENCIA").setMessage("¿Estás seguro que deseas eliminar este detalle de compra?")
                    .setPositiveButton("Sí"){dialog,which->
                        eliminar(idDetalle?.text.toString())
                    }.setNeutralButton("No"){dialog,which->
                        return@setNeutralButton
                    }.show()
            }
        }
        regresar?.setOnClickListener{
            finish()
        }
    }
    fun buscarDetalles(){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM DETALLECOMPRA"
            var resultado = transaccion.rawQuery(SQL,null)
            var cadena=""
            while(resultado.moveToNext()){
                cadena = cadena + resultado.getString(0)+"           "+resultado.getString(1)+"                 "+resultado.getString(2)+"             "+resultado.getString(3)+"             "+resultado.getString(4)+"\n"
            }
            etiqueta?.setText(cadena)
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun insertar(idAlmacen:String, cantidad:String, precio:String, idCompra:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "INSERT INTO DETALLECOMPRA VALUES(null,$idAlmacen,$cantidad,$precio,$idCompra)"
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El detalle de la compra se insertó correctamente.")
            limpiarCampos()
            buscarDetalles()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo insertar el detalle de la compra.")
        }
    }
    fun buscarId(idDetalle:String){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM DETALLECOMPRA WHERE IDDETALLE="+idDetalle
            var resultado = transaccion.rawQuery(SQL,null)
            if(resultado.moveToFirst()){
                idAlmacen?.setText(resultado.getString(1))
                cantidad?.setText(resultado.getString(2))
                precio?.setText(resultado.getString(3))
                idCompra?.setText(resultado.getString(4))
            }else {
                mensaje("ERROR","No se encontró el id del detalle de la compra.")
            }
            transaccion.close()

        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun actualizar(idDetalle:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "UPDATE DETALLECOMPRA SET IDALMACEN="+idAlmacen?.text.toString()+", CANTIDAD="+cantidad?.text.toString()+", PRECIO="+precio?.text.toString()+", IDCOMPRA="+idCompra?.text.toString()+" WHERE IDDETALLE="+idDetalle
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El detalle de la compra se actualizó correctamente.")
            limpiarCampos()
            buscarDetalles()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo actualizar el detalle de la compra.")
        }
    }
    fun eliminar(idDetalle:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "DELETE FROM DETALLECOMPRA WHERE IDDETALLE="+idDetalle
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El detalle de la compra se eliminó correctamente.")
            limpiarCampos()
            buscarDetalles()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo eliminar el detalle de la compra.")
        }
    }
    fun limpiarCampos(){
        idDetalle?.setText("")
        idAlmacen?.setText("")
        cantidad?.setText("")
        precio?.setText("")
        idCompra?.setText("")
    }
    fun mensaje(titulo:String, mensaje:String){
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
    }
}
