package com.connexta.interview.data

import com.connexta.db.artifact.tables.CandJob.CAND_JOB
import com.connexta.db.artifact.tables.CandNotes.CAND_NOTES
import com.connexta.db.artifact.tables.CandRefertype.CAND_REFERTYPE
import com.connexta.db.artifact.tables.CandSchool.CAND_SCHOOL
import com.connexta.db.artifact.tables.Candidate.CANDIDATE
import com.connexta.db.artifact.tables.EmpReferral.EMP_REFERRAL
import com.connexta.db.artifact.tables.Refertype.REFERTYPE
import org.jooq.DSLContext
import java.util.*

data class ReferType(override val id: Int, var name: String) : IdObject

data class School(val school: String,
                  val gradDate: Date)

data class Job(val employer: String,
               val startDate: Date,
               val endDate: Date)

data class CandidateNote(val employee: Employee,
                         val date: Date,
                         val notes: String)

data class Candidate(override val id: Int,
                     var email: String,
                     var phone: String,
                     var name: String,
                     var experience: Int,
                     var referType: ReferType,
                     var referrals: List<Employee>,
                     var schools: List<School>,
                     var jobs: List<Job>,
                     var notes: List<CandidateNote>,
                     var resume: ByteArray) : IdObject {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

// Extension functions

fun ReferType.list(): List<ReferType> {
    return useConnection { ctx ->
        ctx.selectFrom(REFERTYPE)
                .fetch {
                    ReferType(it.id, it.name)
                }
    }
}

fun ReferType.updateOrCreate(): ReferType {
    return useConnection { ctx ->
        if (this.isNew()) {
            val record = ctx.newRecord(REFERTYPE)
            with(record) {
                name = this@updateOrCreate.name
                store()
            }
            this.copy(id = record.id)
        } else {
            ctx.update(REFERTYPE)
                    .set(REFERTYPE.NAME, this@updateOrCreate.name)
                    .where(REFERTYPE.ID.eq(this@updateOrCreate.id))
            this
        }
    }
}

fun ReferType.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(REFERTYPE)
                .where(REFERTYPE.ID.eq(this.id))
    }
}

fun Candidate.create(): Candidate {
    return useConnection { ctx ->
        val record = ctx.newRecord(CANDIDATE)
        // Create the candidate record
        with(record) {
            email = this@create.email
            phone = this@create.phone
            name = this@create.name
            experience = this@create.experience
            setResume(*this@create.resume)
            store()
        }

        // Then create the relationship records
        createCandReferType(ctx, record.id, referType.id)

        this@create.referrals.forEach {
            createReferral(ctx, record.id, it.id)
        }

        this@create.schools.forEach {
            createSchool(ctx, record.id, it)
        }

        this@create.jobs.forEach {
            createJob(ctx, record.id, it)
        }

        this@create.notes.forEach {
            createNote(ctx, record.id, it)
        }

        this.copy(id = record.id)
    }
}

fun Candidate.update(): Candidate {
    return useConnection { ctx ->
        // Update the candidate record
        val candRec = ctx.selectFrom(CANDIDATE)
                .where(CANDIDATE.ID.eq(this@update.id))
                .forUpdate()
                .fetchOne()

        with(candRec) {
            email = this@update.email
            phone = this@update.phone
            name = this@update.name
            experience = this@update.experience
            setResume(*this@update.resume)
            store()
        }

        // Then destructively create the relationship records
        deleteCandReferTypes(ctx, this@update.id)
        createCandReferType(ctx, this@update.id, this@update.referType.id)

        deleteReferrals(ctx, this@update.id)
        this@update.referrals.forEach {
            createReferral(ctx, this@update.id, it.id)
        }

        deleteSchools(ctx, this@update.id)
        this@update.schools.forEach {
            createSchool(ctx, this@update.id, it)
        }

        deleteJobs(ctx, this@update.id)
        this@update.jobs.forEach {
            createJob(ctx, this@update.id, it)
        }

        deleteNotes(ctx, this@update.id)
        this@update.notes.forEach {
            createNote(ctx, this@update.id, it)
        }

        this
    }
}

fun Candidate.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(CANDIDATE)
                .where(CANDIDATE.ID.eq(this.id))
    }
}

private fun createCandReferType(ctx: DSLContext, candidateId: Int, referType: Int) {
    val crRecord = ctx.newRecord(CAND_REFERTYPE)
    with(crRecord) {
        candId = candidateId
        referId = referType
        store()
    }
}

private fun deleteCandReferTypes(ctx: DSLContext, candidateId: Int) {
    ctx.deleteFrom(CAND_REFERTYPE)
            .where(CAND_REFERTYPE.CAND_ID.eq(candidateId))
}

private fun createReferral(ctx: DSLContext, candidateId: Int, employeeId: Int) {
    val refRecord = ctx.newRecord(EMP_REFERRAL)
    with(refRecord) {
        empId = employeeId
        candId = candidateId
        store()
    }
}

private fun deleteReferrals(ctx: DSLContext, candidateId: Int) {
    ctx.deleteFrom(EMP_REFERRAL)
            .where(EMP_REFERRAL.CAND_ID.eq(candidateId))
}

private fun createSchool(ctx: DSLContext, candidateId: Int, sch: School) {
    val schRecord = ctx.newRecord(CAND_SCHOOL)
    with(schRecord) {
        candId = candidateId
        school = sch.school
        gradDate = java.sql.Date(sch.gradDate.time)
        store()
    }
}

private fun deleteSchools(ctx: DSLContext, candidateId: Int) {
    ctx.deleteFrom(CAND_SCHOOL)
            .where(CAND_SCHOOL.CAND_ID.eq(candidateId))
}

private fun createJob(ctx: DSLContext, candidateId: Int, job: Job) {
    val jobRecord = ctx.newRecord(CAND_JOB)
    with(jobRecord) {
        candId = candidateId
        employer = job.employer
        startDate = java.sql.Date(job.startDate.time)
        endDate = java.sql.Date(job.endDate.time)
        store()
    }
}

private fun deleteJobs(ctx: DSLContext, candidateId: Int) {
    ctx.deleteFrom(CAND_JOB)
            .where(CAND_JOB.CAND_ID.eq(candidateId))
}

private fun createNote(ctx: DSLContext, candidateId: Int, note: CandidateNote) {
    val noteRecord = ctx.newRecord(CAND_NOTES)
    with(noteRecord) {
        candId = candidateId
        empId = note.employee.id
        date = java.sql.Timestamp(note.date.time)
        notes = note.notes
        store()
    }
}

private fun deleteNotes(ctx: DSLContext, candidateId: Int) {
    ctx.deleteFrom(CAND_NOTES)
            .where(CAND_NOTES.CAND_ID.eq(candidateId))
}

