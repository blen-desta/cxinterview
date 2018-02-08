package com.connexta.interview.graphql.schema

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

val CustomDateScalar = GraphQLScalarType("Date", "Date with MM/dd/yyyy format.", object : Coercing<Date, String> {
    override fun serialize(input: Any): String? = when (input) {
        is String -> input
        is Date -> input.toString()
        else -> null
    }

    override fun parseValue(input: Any): Date? = parseLiteral(input)

    override fun parseLiteral(input: Any): Date?  = when (input){
        is StringValue -> SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(input.value)
        else -> null
    }
})

val Timestamp = GraphQLScalarType("Timestamp", "Timestamp with yyyy-MM-dd hh:mm:ss.SSS format.", object : Coercing<Timestamp, String> {
    override fun serialize(input: Any): String? = when (input) {
        is String -> input
        is Timestamp -> input.toString()
        else -> null
    }

    override fun parseValue(input: Any): Timestamp? = parseLiteral(input)

    override fun parseLiteral(input: Any): Timestamp?  = when (input){
        is StringValue -> {
            val parsedDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ENGLISH).parse(input.value)
            java.sql.Timestamp.from(parsedDate.toInstant())
        }
        else -> null
    }
})

//Todo: destab - 8/24/2017 - Add Document to the schema with implementation similar to Date and Timestamp
