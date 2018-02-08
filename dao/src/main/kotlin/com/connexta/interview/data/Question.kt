package com.connexta.interview.data

import com.connexta.db.artifact.tables.Category.CATEGORY
import com.connexta.db.artifact.tables.Level.LEVEL
import com.connexta.db.artifact.tables.Question.QUESTION
import com.connexta.db.artifact.tables.QuestionCategory.QUESTION_CATEGORY
import com.connexta.db.artifact.tables.QuestionLevel.QUESTION_LEVEL
import org.jooq.DSLContext

data class Category(override val id: Int, val name: String) : IdObject

data class Level(override val id: Int, val name: String) : IdObject

//Todo: destab - 02/08/2018 - Would be nice to have a list of levels instead
data class Question(override val id: Int,
                    val title: String,
                    val text: String,
                    val notes: String,
                    val level: Level,
                    val categories: List<Category>) : IdObject

// Extension functions

fun Category.list(): List<Category> {
    return useConnection { ctx ->
        ctx.selectFrom(CATEGORY)
                .fetch {
                    Category(it.id, it.name)
                }
    }
}

fun Category.updateOrCreate(): Category {
    return useConnection { ctx ->
        if (this.isNew()) {
            val record = ctx.newRecord(CATEGORY)
            with(record) {
                name = this@updateOrCreate.name
                store()
            }
            this.copy(id = record.id)
        } else {
            ctx.update(CATEGORY)
                    .set(CATEGORY.NAME, this@updateOrCreate.name)
                    .where(CATEGORY.ID.eq(this@updateOrCreate.id))
            this
        }
    }
}

fun Category.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(CATEGORY)
                .where(CATEGORY.ID.eq(this.id))
    }
}

fun Level.list(): List<Level> {
    return useConnection { ctx ->
        ctx.selectFrom(LEVEL)
                .fetch {
                    Level(it.id, it.name)
                }
    }
}

fun Level.updateOrCreate(): Level {
    return useConnection { ctx ->
        if (this.isNew()) {
            val record = ctx.newRecord(LEVEL)
            with(record) {
                name = this@updateOrCreate.name
                store()
            }
            this.copy(id = record.id)
        } else {
            ctx.update(LEVEL)
                    .set(LEVEL.NAME, this@updateOrCreate.name)
                    .where(LEVEL.ID.eq(this@updateOrCreate.id))
            this
        }
    }
}

fun Level.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(LEVEL)
                .where(LEVEL.ID.eq(this.id))
    }
}

fun Question.create(): Question {
    return useConnection { ctx ->
        val record = ctx.newRecord(QUESTION)
        // Create the Question record
        with(record) {
            title = this@create.title
            qtext = this@create.text
            qnotes = this@create.notes
            store()
        }
        // Then create the relationship records
        // one level
        createQuestionLevel(ctx, record.id, level.id)
        // and N categories
        this@create.categories.forEach { cat ->
            createQuestionCategory(ctx, record.id, cat.id)
        }

        this.copy(id = record.id)
    }
}

fun Question.update(): Question {
    return useConnection { ctx ->
        // Update the question record
        ctx.update(QUESTION)
                .set(QUESTION.TITLE, this@update.title)
                .set(QUESTION.QTEXT, this@update.text)
                .set(QUESTION.QNOTES, this@update.notes)
                .where(QUESTION.ID.eq(this@update.id))

        // Then destructively create the relationship records
        // one level
        deleteQuestionLevels(ctx, this@update.id)
        createQuestionLevel(ctx, this@update.id, this@update.level.id)

        // multilevel
        deleteQuestionCategories(ctx, this@update.id)
        this@update.categories.forEach {
            createQuestionCategory(ctx, this@update.id, it.id)
        }

        this
    }
}

fun Question.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(QUESTION)
                .where(QUESTION.ID.eq(this.id))
    }
}

private fun createQuestionLevel(ctx: DSLContext, questionId: Int, levelId: Int) {
    val qlRecord = ctx.newRecord(QUESTION_LEVEL)
    with(qlRecord) {
        qid = questionId
        level = levelId
        store()
    }
}

private fun deleteQuestionLevels(ctx: DSLContext, questionId: Int) {
    ctx.deleteFrom(QUESTION_LEVEL)
            .where(QUESTION_LEVEL.QID.eq(questionId))
}

private fun createQuestionCategory(ctx: DSLContext, questionId: Int, categoryId: Int) {
    val qcRecord = ctx.newRecord(QUESTION_CATEGORY)
    with(qcRecord) {
        qid = questionId
        category = categoryId
        store()
    }
}

private fun deleteQuestionCategories(ctx: DSLContext, questionId: Int) {
    ctx.deleteFrom(QUESTION_CATEGORY)
            .where(QUESTION_CATEGORY.QID.eq(questionId))
}
