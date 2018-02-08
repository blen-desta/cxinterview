package com.connexta.interview.graphql.schema

import com.connexta.interview.data.CandidateNote
import com.connexta.interview.data.Employee
import com.connexta.interview.data.Job
import com.connexta.interview.data.School
import java.util.*

val employees = mutableListOf(
        Employee(1, "A"),
        Employee(2, "B"),
        Employee(3, "C"),
        Employee(4, "D"),
        Employee(5, "E"),
        Employee(6, "F")
)

val questions = mutableListOf(
        Question(0, "Sudo", "What is sudo and why would you use it?", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS)),
        Question(1, "Timezones", "How do you change timezones?", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS)),
        Question(2, "Linux Services", "What are they? How do you create, start, stop?", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS)),
        Question(3, "HTTP Error Codes", "Explain HTTP Error Codes - what is the difference between the 200's, 400's, 500's?", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS)),
        Question(4, "Public/private key", "What tool would you use to generate a public/private key pair?", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS)),
        Question(5, "Java application", "With a java application, how would you set up certificates? What is a keystore? What is a truststore?", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS)),
        Question(6, "Nexus", "What is Nexus, NPM and what are they used for?", "", QuestionLevel.NEWGRAD, listOf(QuestionCategory.DEVOPS))
)

val empNotes : EmployeeNotes = EmployeeNotes(1, employees[1], "")
val quesNotes : QuestionNotes = QuestionNotes(1, questions[1], employees[2], "Great Answer!")
val interviewSession : Interview = Interview(1, SessionType.EMPLOYEE, empNotes, quesNotes, "")
val date = Date(63040464000000)
val school : School = School("ASU", date)
val job : Job = Job( "Non-Connexta", date, date)
val candidateNotes : CandidateNote = CandidateNote(employees[5], date, "Interesting conversation, good candidate")

val candidates = mutableListOf(
        Candidate(0, "A", "a@important.com","480-487-7890", 1, interviewSession, employees[5], ReferType.UACAREER, school, job, candidateNotes),
        Candidate(1, "B", "b@important.com","480-487-7891", 2, interviewSession, employees[3], ReferType.UACAREER, school, job, candidateNotes),
        Candidate(2, "C", "c@important.com","480-487-7892", 1, interviewSession, employees[1], ReferType.UACAREER, school, job, candidateNotes),
        Candidate(3, "D", "d@important.com","480-487-7893", 2, interviewSession, employees[4], ReferType.UACAREER, school, job, candidateNotes)
)