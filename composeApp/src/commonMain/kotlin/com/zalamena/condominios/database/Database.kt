package com.zalamena.condominios.database

import com.zalamena.condominios.moradores.data.dao.MoradoresDao

interface Database {


    fun moradoresDao(): MoradoresDao
}