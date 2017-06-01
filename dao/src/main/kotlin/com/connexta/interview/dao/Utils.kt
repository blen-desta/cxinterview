package com.connexta.interview.dao

import com.connexta.db.artifact.tables.Category
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Table
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

fun getConnection(): Connection {
    val properties = Properties()
    properties.load(properties::class.java.getResourceAsStream("/db.properties"));

    Class.forName(properties.getProperty("db.driver"))
    return DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.username"),
            properties.getProperty("db.password")
    )
}

fun header(text: String) {
    println()
    println(text)
    println(text.toCharArray().map { _ -> '-' }.joinToString(separator = ""))
}

fun <R: Record, T: Table<R>> T.findId(ctx: DSLContext, condition: Condition?): Int {
    return ctx.select(this.field("id", 1.javaClass))
            .from(this)
            .where(condition)
            .fetchOne()
            .value1()
}
