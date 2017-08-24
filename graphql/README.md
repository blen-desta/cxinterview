# Preliminary GraphQL Schema
This is a first draft of the initial schema to be built.

```graphql
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
  PHONE
  VIDEO
  ONSITE
}

type Question {
  id: Int!
  title: String!
  text: String!
  notes: String
  levels: [QuestionLevel!]
  categories: [QuestionCategory!]
}

type Employee {
  id: Int!
  username: String!
}

type School {
  name: String!
  gradDate: Date!
}

type Job {
  name: String!
  startDate: Date!
  endDate: Date!
}

scalar Date

scalar Timestamp

scalar Document

type Candidate {
  id: Int!
  name: String!
  email: String!
  phone: String!
  yearsExperience: Int!
  interviewSessions: [Interview!]
  employeeReferrals: [Employee!]
  referralType: ReferType
  schools: [School!]
  previousJobs: [Job!]
  generalNotes: [CandidateNotes!]
  resume: Document
}

type Interview {
  id: Int!
  sessionType: SessionType!
  date: Timestamp!
  employeeNotes: [EmployeeNotes!]
  questionsAsked: [QuestionNotes!]
  recommendation: String
}

type EmployeeNotes {
  employee: Employee!
  notes: String
}

type QuestionNotes {
  question: Question!
  employee: Employee!
  notes: String
}

type CandidateNotes {
  employee: Employee!
  date: Date!
  notes: String!
}

type Query {
  question(id: Int, level: QuestionLevel, category: QuestionCategory): [Question]

  employee(username: String): Employee

  candidate(nameLike: String,
            emailLike: String,
            referredBy: Employee,
            referType: ReferType,
            school: String,
            job: String,
            interviewDate: Date): [Candidate]
}

type Mutation {
  createQuestion(Question!)
  updateQuestion(Question!)

  createEmployee(Employee!)
  updateEmployee(Employee!)

  createCandidate(Candidate!)
  updateCandidate(Candidate!)

  createInterview(Interview!)
  updateInterview(Interview!)
  deleteInterview(Interview!)
}
```