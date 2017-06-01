package com.connexta.interview.dao

import com.connexta.db.artifact.tables.Candidate.CANDIDATE
import com.connexta.db.artifact.tables.Category.CATEGORY
import com.connexta.db.artifact.tables.Employee.EMPLOYEE
import com.connexta.db.artifact.tables.Level.LEVEL
import com.connexta.db.artifact.tables.Question.QUESTION
import com.connexta.db.artifact.tables.QuestionCategory.QUESTION_CATEGORY
import com.connexta.db.artifact.tables.QuestionLevel.QUESTION_LEVEL
import com.connexta.db.artifact.tables.Session.SESSION
import com.connexta.db.artifact.tables.SessionQuestionNotes.SESSION_QUESTION_NOTES
import com.connexta.db.artifact.tables.Sessiontype.SESSIONTYPE
import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.io.File
import java.sql.Timestamp
import java.util.*


fun main(args: Array<String>) {
    getConnection().use {
        val ctx = DSL.using(it)

        val type = SESSIONTYPE
        header("Stuff and others")
        ctx.select(type.ID, type.NAME)
                .from(type)
                .orderBy(2)
                .forEach {
                    println("ID: ${it[type.ID]} / Name: ${it[type.NAME]}")
                }

        createUser(ctx)
        val qid = createQuestion(ctx)
        createSession(ctx, qid!!)

    }
}

fun createUser(ctx: DSLContext) {
    val emp = ctx.newRecord(EMPLOYEE)
    with(emp) {
        emp.username = "rporter"
        emp.store()
    }
}


fun createQuestion(ctx: DSLContext): Int? {
    val catId = CATEGORY.findId(ctx, CATEGORY.NAME.eq("BACKEND"))
    val levelId = LEVEL.findId(ctx, LEVEL.NAME.eq("NEW_GRAD"))

    val qst = ctx.newRecord(QUESTION)
    with(qst) {
        qtext = "What is your favorite color"
        qnotes = "Blue is a good answer"
        qst.store()
    }


    val qlevel = ctx.newRecord(QUESTION_LEVEL)
    with(qlevel) {
        qid = qst.id
        level = levelId
        store()
    }

    val qcat = ctx.newRecord(QUESTION_CATEGORY)
    with(qcat) {
        qid = qst.id
        category = catId
        store()
    }

    return qst.id
}

fun createSession(ctx: DSLContext, qid: Int) {
    val typeId = SESSIONTYPE.findId(ctx, SESSIONTYPE.NAME.eq("PHONE"))
    val empId = EMPLOYEE.findId(ctx, EMPLOYEE.USERNAME.eq("rporter"))

    val cand = ctx.newRecord(CANDIDATE)
    with (cand) {
        name = "Joe Blow"
        experience = 12
        phone = "8675309"
        email = "jenny@dontlosemynumber.com"
        val readBytes = File("/Users/rporter/projects/connexta/interview/dao/src/main/resources/Andrew Cross from CyberCoders - Resume.pdf").readBytes()
        setResume(*readBytes)
        store()
    }

    val session = ctx.newRecord(SESSION)
    with(session) {
        candId = cand.id
        type = typeId
        date = Timestamp(Date().time)
        recommendation = "This rocks"
        store()
    }

    val qnotes = ctx.newRecord(SESSION_QUESTION_NOTES)
    qnotes.let {
        it.sessionId = session.id
        it.empId = empId
        it.qid = qid
        it.notes = "This was a great answer"
        it.store()
    }

}



