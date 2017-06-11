package com.connexta.interview.data

import com.connexta.db.artifact.tables.Session.SESSION
import com.connexta.db.artifact.tables.SessionNotes.SESSION_NOTES
import com.connexta.db.artifact.tables.SessionQuestionNotes.SESSION_QUESTION_NOTES
import com.connexta.db.artifact.tables.Sessiontype.SESSIONTYPE
import org.jooq.DSLContext
import java.sql.Timestamp
import java.util.*

data class SessionType(override val id: Int, val name: String) : IdObject

data class QuestionNote(val question: Question,
                        val notes: String)

data class SessionNotes(val employee: Employee,
                        val notes: String)

data class SessionQuestionNotes(val employee: Employee,
                                val notes: List<QuestionNote>)

data class Session(override val id: Int,
                   val candidate: Candidate,
                   val type: SessionType,
                   val date: Date,
                   val sessionNotes: List<SessionNotes>,
                   val questionNotes: List<SessionQuestionNotes>,
                   val recommendation: String) : IdObject

fun SessionType.list(): List<SessionType> {
    return useConnection { ctx ->
        ctx.selectFrom(SESSIONTYPE)
                .fetch {
                    SessionType(it.id, it.name)
                }
    }
}

fun SessionType.updateOrCreate(): SessionType {
    return useConnection { ctx ->
        if (this.isNew()) {
            val record = ctx.newRecord(SESSIONTYPE)
            with(record) {
                name = this@updateOrCreate.name
                store()
            }
            this.copy(id = record.id)
        } else {
            ctx.update(SESSIONTYPE)
                    .set(SESSIONTYPE.NAME, this@updateOrCreate.name)
                    .where(SESSIONTYPE.ID.eq(this@updateOrCreate.id))
            this
        }
    }
}

fun SessionType.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(SESSIONTYPE)
                .where(SESSIONTYPE.ID.eq(this.id))
    }
}

fun Session.create(): Session {
    return useConnection { ctx ->
        val record = ctx.newRecord(SESSION)
        // Create the Session record
        with(record) {
            candId = this@create.candidate.id
            type = this@create.type.id
            date = Timestamp(this@create.date.time)
            recommendation = this@create.recommendation
        }

        // Then create the relationship records
        this@create.sessionNotes.forEach {
            createSessionNotes(ctx, record.id, it)
        }

        this@create.questionNotes.forEach {
            createSessionQuestionNotes(ctx, record.id, it)
        }

        this.copy(id = record.id)
    }
}

fun Session.update(): Session {
    return useConnection { ctx ->
        ctx.update(SESSION)
                .set(SESSION.CAND_ID, this@update.candidate.id)
                .set(SESSION.TYPE, this@update.type.id)
                .set(SESSION.DATE, Timestamp(this@update.date.time))
                .set(SESSION.RECOMMENDATION, this@update.recommendation)
                .where(SESSION.ID.eq(this@update.id))

        // Then destructively create the relationship records
        deleteSessionNotes(ctx, this@update.id)
        this@update.sessionNotes.forEach {
            createSessionNotes(ctx, this@update.id, it)
        }

        deleteSessionQuestionNotes(ctx, this@update.id)
        this@update.questionNotes.forEach {
            createSessionQuestionNotes(ctx, this@update.id, it)
        }

        this
    }
}

fun Session.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(SESSION)
                .where(SESSION.ID.eq(this.id))
    }
}

private fun createSessionNotes(ctx: DSLContext, sessionId: Int, sessionNotes: SessionNotes) {
    val snRecord = ctx.newRecord(SESSION_NOTES)
    with(snRecord) {
        this.sessionId = sessionId
        empId = sessionNotes.employee.id
        notes = sessionNotes.notes
        store()
    }
}

private fun deleteSessionNotes(ctx: DSLContext, sessionId: Int) {
    ctx.deleteFrom(SESSION_NOTES)
            .where(SESSION_NOTES.SESSION_ID.eq(sessionId))
}

private fun createSessionQuestionNotes(ctx: DSLContext, sessionId: Int, sessionNotes: SessionQuestionNotes) {
    sessionNotes.notes.forEach { qn ->
        val qnRecord = ctx.newRecord(SESSION_QUESTION_NOTES)
        with(qnRecord) {
            this.sessionId = sessionId
            empId = sessionNotes.employee.id
            qid = qn.question.id
            notes = qn.notes
            store()
        }
    }
}

private fun deleteSessionQuestionNotes(ctx: DSLContext, sessionId: Int?) {
    ctx.deleteFrom(SESSION_QUESTION_NOTES)
            .where(SESSION_QUESTION_NOTES.SESSION_ID.eq(sessionId))
}