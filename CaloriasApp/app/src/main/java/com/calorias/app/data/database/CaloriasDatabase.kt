package com.calorias.app.data.database

import android.content.Context
import androidx.room.*
import com.calorias.app.data.dao.RefeicaoDao
import com.calorias.app.data.model.Refeicao
import com.calorias.app.data.model.TipoRefeicao

class TipoRefeicaoConverter {
    @TypeConverter
    fun fromTipo(tipo: TipoRefeicao): String = tipo.name

    @TypeConverter
    fun toTipo(nome: String): TipoRefeicao = TipoRefeicao.valueOf(nome)
}

@Database(entities = [Refeicao::class], version = 1, exportSchema = false)
@TypeConverters(TipoRefeicaoConverter::class)
abstract class CaloriasDatabase : RoomDatabase() {

    abstract fun refeicaoDao(): RefeicaoDao

    companion object {
        @Volatile
        private var INSTANCE: CaloriasDatabase? = null

        fun getDatabase(context: Context): CaloriasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CaloriasDatabase::class.java,
                    "calorias_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
