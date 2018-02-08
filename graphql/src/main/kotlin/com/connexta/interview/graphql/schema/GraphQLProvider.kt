package com.connexta.interview.graphql.schema

import com.connexta.interview.data.*
import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import java.io.File
import java.util.*

fun createSchema() = SchemaParser.newParser()
        .schemaString(schemaDefinition)
        .resolvers(Query(), Mutation())
        .scalars(CustomDateScalar)
        .build()
        .makeExecutableSchema()

val schemaDefinition = File("graphql/src/main/resources/schema-definition.txt").inputStream().bufferedReader().use { it.readText() }

//Enums
//Todo: destab - 8/24/2017 - Enums are already defined in the dao module as data class. Fix it!
enum class QuestionCategory { DEVOPS, FRONTEND, BACKEND }
enum class QuestionLevel { NEWGRAD, JUNIOR, SENIOR }
enum class ReferType {EMPLOYEE, RECRUITER, ASUCAREER, UACAREER }
enum class SessionType {EMPLOYEE, ASUCAREER, UACAREER }

//Types
//Todo: destab - 8/24/2017 - Change use of these types to existing implementations in dao module
data class Question(override val id: Int,
                    val title: String,
                    val text: String,
                    val notes: String?,
                    val level: QuestionLevel,
                    val categories: List<QuestionCategory>) : IdObject
//Todo: destab - 9/21/2017 - Remember to add date in implementation and schema once Timestamp scalar is implemented
data class Interview (val id: Int, val sessionType: SessionType, val employeeNotes: EmployeeNotes, val questionsAsked: QuestionNotes, val recommendation: String)
//Todo: destab - 9/21/2017 - Remember to add resume in implementation and schema once Document scalar is implemented
data class Candidate(val id: Int, val name: String, val email: String, val phone: String, val yearsExperience: Int, val interviewSessions: Interview,
                     val employeeReferrals: Employee, val referralType: ReferType?, val schools: School, val previousJobs: Job, val generalNotes: CandidateNote)
data class EmployeeNotes(val id: Int, val employee: Employee, val notes: String?)
data class QuestionNotes(val id: Int, val question: Question, val employee: Employee, val notes: String?)

abstract class ListListResolver<out E> {
    fun listList(): List<List<E>> = listOf(listOf())
}

class Query: GraphQLQueryResolver, ListListResolver<String>(){
    fun question(id: Int?, level: QuestionLevel?, category: QuestionCategory?): List<Question> =
            if (id != null) {
                if (level != null) {
                    if (category != null) {
                        questions.filter { it.id == id && it.level.name.equals(level) && it.categories.get(0).name.equals(category) }
                    }
                    questions.filter { it.id == id && it.level.name.equals(level) }
                }
                questions.filter { it.id == id }
            } else {
                if (level != null) {
                    if (category != null) {
                        questions.filter { it.level.name.equals(level) && it.categories.get(0).name.equals(category) }
                    }
                    questions.filter { it.level.name.equals(level) }
                } else {
                    if (category != null) {
                        questions.filter { it.categories.get(0).name.equals(category) }
                    }
                    mutableListOf(Question(0,"ERROR", "No such question in the database", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS)))
                }
            }


    fun allQuestions(): List<Question> = questions

    fun employee(username: String) : List<Employee> = employees.filter { it.username == username }

    fun allEmployees(): List<Employee> = employees

    //Todo: destab - 8/24/2017 - Incorrect implementations
    fun candidate(name: String, email: String, referredBy: Employee, referType: ReferType, school: String, job: String, interviewDate: Date) : List<Candidate> = candidates

    fun allCandidates(): List<Candidate> = candidates
}

class Mutation : GraphQLMutationResolver {
    fun createQuestion(id: Int, title: String, text: String, notes: String?, level: QuestionLevel, category: List<QuestionCategory>) : String {
        if (questions.filter { it.id == id }.isEmpty()) {
            questions.add(Question(id, title, text, notes, level, category))
            return "Question added Successfully"
        }
        else{
            return "ERROR: A question with that id already exists."
        }
    }

    fun updateQuestion(id: Int, title: String, text: String, notes: String?, level: QuestionLevel, category: List<QuestionCategory>) : String {
        if (questions.filter { it.id == id }.isNotEmpty()) {
            questions.removeAll(questions.filter { it.id == id })
            questions.add(Question(id, title, text, notes, level, category))
            return "Successfully updated question."
        }
        else{
            return "ERROR: Unable to update question. No question with provided ID exists."
        }
    }

    fun createEmployee(id: Int, username: String) : String {
        if (questions.filter { it.id == id }.isEmpty()) {
            employees.add(Employee(id, username))
            return "Employee added Successfully"
        }
        else{
            return "ERROR: An employee with that id already exists."
        }
    }

    fun updateEmployee(id: Int, username: String) : String {
        if (employees.filter { it.id == id }.isNotEmpty()) {
            employees.removeAll(employees.filter { it.id == id })
            employees.add(Employee(id, username))
            return "Successfully updated employee."
        }
        else{
            return "ERROR: Unable to update employee. No employee with provided ID exists."
        }
    }

    //Todo: destab - 8/24/2017 - Implementations for creating, updating and deleting interviews missing
    //    createCandidate(id: Int!, name: String!, email: String!, phone: String!, yearsExperience: Int!, interviewSessionsId: Int!, employeeReferralsId: Int!, referralType: ReferType, schoolsId: Int!, previousJobsId: Int!, generalNotes: CandidateNotesInt!) : String
    //    updateCandidate(id: Int!, name: String!, email: String!, phone: String!, yearsExperience: Int!, interviewSessionsId: Int!, employeeReferralsId: Int!, referralType: ReferType, schoolsId: Int!, previousJobsId: Int!, generalNotes: CandidateNotesInt!) : String
    //    createInterview(Interview!)
    //    updateInterview(Interview!)
    //    deleteInterview(Interview!)
}