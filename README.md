# SQLITE 🦦

>[!NOTE]
> ***EL SIGUIENTE CÓDIGO DEMUESTRA OPERACIONES BÁSICAS CON SQL SIGUIENDO LA GUÍA: https://developer.android.com/training/data-storage/sqlite?hl=es-419 | PARA PROBAR QUE EL PROGRAMA FUNCIONE CORRECTAMENTE DEBEREMOS CORRERLO Y COMPROBAR EL LOGCAT CONFORME FUNCIONARON TODAS LAS SENTENCIAS SQL 🦂***

### ARCHIVOS PRINCIPALES 🐟

#### ContratoDB.kt 🐏

> ***DEFINE LA ESTRUCTURA DE LA BASE DE DATOS***

```kotlin
object ContratoDB {
    object Estudiantes {
        const val TABLA = "estudiantes"
        const val ID = "_id"
        const val NOMBRE = "nombre"
        const val EDAD = "edad"
        const val CARRERA = "carrera"
    }
}
```

---

#### DatabaseHelper.kt 🏹

> ***MANEJA LA CREACIÓN O LA ACTUALIZACIÓN DE LA BASE DE DATOS (CREA LAS 4 COLUMNAS)***

```kotlin
class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    "Escuela.db",
    null,
    1
) {

    private val SQL_CREAR_TABLA = """
        CREATE TABLE ${ContratoDB.Estudiantes.TABLA} (
            ${ContratoDB.Estudiantes.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${ContratoDB.Estudiantes.COLUMNA_NOMBRE} TEXT NOT NULL,
            ${ContratoDB.Estudiantes.COLUMNA_EDAD} INTEGER NOT NULL,
            ${ContratoDB.Estudiantes.COLUMNA_CARRERA} TEXT
        )
    """.trimIndent()

    private val SQL_BORRAR_TABLA = """
        DROP TABLE IF EXISTS ${ContratoDB.Estudiantes.TABLA}
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREAR_TABLA)
        Log.d("SQLiteTest", "TABLA CREADA: ${ContratoDB.Estudiantes.TABLA}")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_BORRAR_TABLA)
        onCreate(db)
        Log.d("SQLiteTest", "BD ACTUALIZADA DE $oldVersion A $newVersion")
    }
}
```

---

#### MainActivity.kt 🦁

> ***EJECUTA LAS OPERACIONES CRUD AUTOMÁTICAMENTE AL INICIAR (EN MIS ARCHIVOS; INSERTA DOS ESTUDIANTES, CONSULTA, ACTUALIZA LA EDAD DE ESOS ESTUDIANTES, ELIMINA UNO, VUELVE A CONSULTAR)***

```kotlin
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
```
