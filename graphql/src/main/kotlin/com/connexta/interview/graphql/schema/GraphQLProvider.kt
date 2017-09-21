package com.connexta.interview.graphql.schema

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import java.text.SimpleDateFormat
import java.util.*

fun createSchema() = SchemaParser.newParser()
        .schemaString(schemaDefinition)
        .resolvers(Query(), Mutation())
        .scalars(CustomDateScalar)
        .build()
        .makeExecutableSchema()

val schemaDefinition = """

enum QuestionCategory {
    DEVOPS
    FRONTEND
    BACKEND
}

enum QuestionLevel {
    NEWGRAD
    JUNIOR
    SENIOR
}

enum ReferType {
    EMPLOYEE
    RECRUITER
    ASUCAREER
    UACAREER
}

enum SessionType {
    EMPLOYEE
    ASUCAREER
    UACAREER
}

type Question {
    id: Int!
    text: String!
    notes: String
    level: QuestionLevel!
    category: QuestionCategory!
}

type Candidate{
    id: Int!
    name: String!
    email: String!
    phone: String!
    yearsExperience: Int!
    interviewSessions: Interview!
    employeeReferrals: Employee!
    referralType: ReferType
    schools: School!
    previousJobs: Job!
    generalNotes: CandidateNotes!
}

type Employee {
    id: Int!
    username: String!
}

type School {
    id: Int!
    name: String!
    gradDate: Date!
}

type Job {
    id: Int!
    name: String!
    startDate: Date!
    endDate: Date!
}

type Interview {
    id: Int!
    sessionType: SessionType!
    employeeNotes: EmployeeNotes!
    questionsAsked: QuestionNotes!
    recommendation: String
}

type EmployeeNotes {
    id: Int!
    employee: Employee!
    notes: String
}

type QuestionNotes{
    id: Int!
    question: Question!
    employee: Employee!
    notes: String
}

type CandidateNotes {
    id: Int!
    employee: Employee!
    date: Date!
    notes: String!
}

scalar Date

type Query {
    question(id: Int, level: QuestionLevel, category: QuestionCategory): [Question!]
    allQuestions: [Question!]
    employee(firstName: String!): [Employee!]
    allEmployees: [Employee!]
    candidate(name: String, email: String, referredBy: String, referType: ReferType, school: String, job: String, interviewDate: Date):[Candidate!]
    allCandidates: [Candidate!]
}

type Mutation {
    createQuestion(id: Int!, text: String!, notes: String, level: QuestionLevel!, category: QuestionCategory!) : String
    updateQuestion(id: Int!, text: String!, notes: String, level: QuestionLevel!, category: QuestionCategory!) : String
    createEmployee(id: Int!, username: String!) : String
    updateEmployee(id: Int!, username: String!) : String
}
"""

//Enums
enum class QuestionCategory { DEVOPS, FRONTEND, BACKEND }
enum class QuestionLevel { NEWGRAD, JUNIOR, SENIOR }
enum class ReferType {EMPLOYEE, RECRUITER, ASUCAREER, UACAREER }
enum class SessionType {EMPLOYEE, ASUCAREER, UACAREER }

//Types
data class Employee(val id: Int, val username: String)
data class School(val id: Int, val name: String, val gradDate: Date)
data class EmployeeNotes(val id: Int, val employee: Employee, val notes: String?)
data class Job(val id: Int, val name: String, val startDate: Date, val endDate: Date)
data class CandidateNotes(val id: Int, val employee: Employee, val date: Date, val notes: String)
data class QuestionNotes(val id: Int, val question: Question, val employee: Employee, val notes: String?)
data class Question(val id: Int, val text: String, val notes: String?, val level: QuestionLevel, val category: QuestionCategory)
//Todo: destab - 9/21/2017 - Remember to add date in implementation and schema once Timestamp scalar is implemented
data class Interview (val id: Int, val sessionType: SessionType, val employeeNotes: EmployeeNotes, val questionsAsked: QuestionNotes, val recommendation: String)
//Todo: destab - 9/21/2017 - Remember to add resume in implementation and schema once Document scalar is implemented
data class Candidate(val id: Int, val name: String, val email: String, val phone: String, val yearsExperience: Int, val interviewSessions: Interview,
                     val employeeReferrals: Employee, val referralType: ReferType?, val schools: School, val previousJobs: Job, val generalNotes: CandidateNotes)


//Scalar Types
val CustomDateScalar = GraphQLScalarType("Date", "Date", object : Coercing<Date, String>{
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

//Todo: destab - 8/24/2017 - Add Timestamp and Document to the schema with implementation similar to Date


abstract class ListListResolver<out E> {
    fun listList(): List<List<E>> = listOf(listOf())
}

class Query: GraphQLQueryResolver, ListListResolver<String>(){
    fun question(id: Int?, level: QuestionLevel?, category: QuestionCategory?): List<Question> =
            if (id != null) {
                if (level != null) {
                    if (category != null) {
                        questions.filter { it.id == id && it.level.name.equals(level) && it.category.name.equals(category) }
                    }
                    questions.filter { it.id == id && it.level.name.equals(level) }
                }
                questions.filter { it.id == id }
            } else {
                if (level != null) {
                    if (category != null) {
                        questions.filter { it.level.name.equals(level) && it.category.name.equals(category) }
                    }
                    questions.filter { it.level.name.equals(level) }
                } else {
                    if (category != null) {
                        questions.filter { it.category.name.equals(category) }
                    }
                    mutableListOf(Question(0, "No such question in the database", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS))
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
    fun createQuestion(id: Int, text: String, notes: String?, level: QuestionLevel, category: QuestionCategory) : String {
        if (questions.filter { it.id == id }.isEmpty()) {
            questions.add(Question(id, text, notes, level, category))
            return "Question added Successfully"
        }
        else{
            return "ERROR: A question with that id already exists."
        }
    }

    fun updateQuestion(id: Int, text: String, notes: String?, level: QuestionLevel, category: QuestionCategory) : String {
        if (questions.filter { it.id == id }.isNotEmpty()) {
            questions.removeAll(questions.filter { it.id == id })
            questions.add(Question(id, text, notes, level, category))
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

//Dummy Data
val employees = mutableListOf(
        Employee(1, "A"),
        Employee(2, "B"),
        Employee(3, "C"),
        Employee(4, "D"),
        Employee(5, "E"),
        Employee(6, "F")
)

val questions = mutableListOf(
        Question(0, "What is sudo and why would you use it?", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS),
        Question(1, "How do you change timezones?", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS),
        Question(2, "Linux Services - What are they? How do you create, start, stop?", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS),
        Question(3, "Explain HTTP Error Codes - what is the difference between the 200's, 400's, 500's?", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS),
        Question(4, "What tool would you use to generate a public/private key pair?", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS),
        Question(5, "With a java application, how would you set up certificates? What is a keystore? What is a truststore?", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS),
        Question(6, "What is Nexus, NPM and what are they used for?", "", QuestionLevel.NEWGRAD, QuestionCategory.DEVOPS)
)

val empNotes : EmployeeNotes = EmployeeNotes(1, employees[1], "")
val quesNotes : QuestionNotes = QuestionNotes(1, questions[1], employees[2], "Great Answer!")
val interviewSession : Interview = Interview(1, SessionType.EMPLOYEE, empNotes, quesNotes, "")
val date = Date(63040464000000)
val school : School = School(1, "ASU", date)
val job : Job = Job(1, "Non-Connexta", date, date)
val candidateNotes : CandidateNotes = CandidateNotes(1, employees[5], date, "Interesting conversation, good candidate")

val candidates = mutableListOf(
        Candidate(0, "A", "a@important.com","480-487-7890", 1, interviewSession, employees[5], ReferType.UACAREER, school, job, candidateNotes),
        Candidate(1, "B", "b@important.com","480-487-7891", 2, interviewSession, employees[3], ReferType.UACAREER, school, job, candidateNotes),
        Candidate(2, "C", "c@important.com","480-487-7892", 1, interviewSession, employees[1], ReferType.UACAREER, school, job, candidateNotes),
        Candidate(3, "D", "d@important.com","480-487-7893", 2, interviewSession, employees[4], ReferType.UACAREER, school, job, candidateNotes)
)