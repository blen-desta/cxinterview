package com.connexta.interview.data

import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Table
import org.jooq.impl.DSL
import java.sql.DriverManager
import java.util.*

interface IdObject {
    val id: Int?

    fun isNew(): Boolean = id == null
}

fun <T> useConnection(body: (ctx: DSLContext) -> T): T {
    val properties = Properties()
    properties.load(properties::class.java.getResourceAsStream("/db.properties"));

    Class.forName(properties.getProperty("db.driver"))
    val connection = DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.username"),
            properties.getProperty("db.password")
    )

    connection.use {
        val ctx = DSL.using(it)
        return body(ctx)
    }
}

fun <R : Record, T : Table<R>> T.findId(ctx: DSLContext, condition: Condition?): Int {
    return ctx.select(this.field("id", 1.javaClass))
            .from(this)
            .where(condition)
            .fetchOne()
            .value1()
}
