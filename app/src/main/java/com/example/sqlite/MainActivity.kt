package com.example.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.DatabaseHelper
import com.example.sqlite.ContratoDB

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // INICIALIZAR BASE DE DATOS
        dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase

        Log.d("SQLiteTest", "INICIANDO PRUEBAS")

        // EJECUTAR PRUEBAS
        insertarDatos()
        consultarDatos()
        actualizarDatos()
        consultarDatos()
        eliminarDatos()
        consultarDatos()

        db.close()
        Log.d("SQLiteTest", "PRUEBAS COMPLETADAS")
    }

    private fun insertarDatos() {
        Log.d("SQLiteTest", "INSERTANDO DATOS...")

        // INSERTAR PRIMER ESTUDIANTE
        val values1 = ContentValues().apply {
            put(ContratoDB.Estudiantes.COLUMNA_NOMBRE, "SAUL ALVAREZ")
            put(ContratoDB.Estudiantes.COLUMNA_EDAD, 20)
            put(ContratoDB.Estudiantes.COLUMNA_CARRERA, "INGENIERIA")
        }
        val id1 = db.insert(ContratoDB.Estudiantes.TABLA, null, values1)
        Log.d("SQLiteTest", "INSERTADO - ID: $id1")

        // INSERTAR SEGUNDO ESTUDIANTE
        val values2 = ContentValues().apply {
            put(ContratoDB.Estudiantes.COLUMNA_NOMBRE, "DAMIAN")
            put(ContratoDB.Estudiantes.COLUMNA_EDAD, 22)
            put(ContratoDB.Estudiantes.COLUMNA_CARRERA, "MEDICINA")
        }
        val id2 = db.insert(ContratoDB.Estudiantes.TABLA, null, values2)
        Log.d("SQLiteTest", "INSERTADO - ID: $id2")
    }

    private fun consultarDatos() {
        Log.d("SQLiteTest", "CONSULTANDO DATOS...")

        // COLUMNAS A CONSULTAR
        val columnas = arrayOf(
            ContratoDB.Estudiantes.ID,
            ContratoDB.Estudiantes.COLUMNA_NOMBRE,
            ContratoDB.Estudiantes.COLUMNA_EDAD,
            ContratoDB.Estudiantes.COLUMNA_CARRERA
        )

        // REALIZAR CONSULTA
        val cursor: Cursor = db.query(
            ContratoDB.Estudiantes.TABLA,
            columnas,
            null,
            null,
            null,
            null,
            null
        )

        // RECORRER RESULTADOS
        with(cursor) {
            Log.d("SQLiteTest", "TOTAL REGISTROS: $count")

            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(ContratoDB.Estudiantes.ID))
                val nombre = getString(getColumnIndexOrThrow(ContratoDB.Estudiantes.COLUMNA_NOMBRE))
                val edad = getInt(getColumnIndexOrThrow(ContratoDB.Estudiantes.COLUMNA_EDAD))
                val carrera = getString(getColumnIndexOrThrow(ContratoDB.Estudiantes.COLUMNA_CARRERA))

                Log.d("SQLiteTest", "ID: $id | NOMBRE: $nombre | EDAD: $edad | CARRERA: $carrera")
            }
            close()
        }
    }

    private fun actualizarDatos() {
        Log.d("SQLiteTest", "ACTUALIZANDO DATOS...")

        val nuevosValores = ContentValues().apply {
            put(ContratoDB.Estudiantes.COLUMNA_EDAD, 21)
        }

        val whereClause = "${ContratoDB.Estudiantes.COLUMNA_NOMBRE} = ?"
        val whereArgs = arrayOf("SAUL ALVAREZ")

        val filasActualizadas = db.update(
            ContratoDB.Estudiantes.TABLA,
            nuevosValores,
            whereClause,
            whereArgs
        )

        Log.d("SQLiteTest", "FILAS ACTUALIZADAS: $filasActualizadas")
    }

    private fun eliminarDatos() {
        Log.d("SQLiteTest", "ELIMINANDO DATOS...")

        val whereClause = "${ContratoDB.Estudiantes.COLUMNA_NOMBRE} = ?"
        val whereArgs = arrayOf("DAMIAN")

        val filasEliminadas = db.delete(
            ContratoDB.Estudiantes.TABLA,
            whereClause,
            whereArgs
        )

        Log.d("SQLiteTest", "FILAS ELIMINADAS: $filasEliminadas")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::db.isInitialized && db.isOpen) {
            db.close()
        }
    }
}