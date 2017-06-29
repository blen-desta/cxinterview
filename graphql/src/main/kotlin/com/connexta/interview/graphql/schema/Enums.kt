package com.connexta.interview.graphql.schema

import graphql.schema.GraphQLEnumType
import graphql.schema.GraphQLEnumType.newEnum

val questionCategory: GraphQLEnumType = newEnum()
        .name("QuestionCategory")
        .description("Area of concern for a question.")
        .value("DEVOPS", "DevOps questions, relating to networking, CI, and tooling")
        .value("FRONTEND", "Front-end focused questions, relating to JavaScript, CSS, and other front-end technologies")
        .value("BACKEND", "Back-end focused questions, relating to APIs, data modeling, business modeling, and similar")
        .build()

val questionLevel: GraphQLEnumType = newEnum()
        .name("QuestionLevel")
        .description("Target level of candidate")
        .value("NEWGRAD", "Question appropriate for a new college graduate or intern")
        .value("JUNIOR", "Question appropriate for a junior engineer with 0-3 years professional experience")
        .value("SENIOR", "Question appropriate for a senior engineer")
        .build()

val referType: GraphQLEnumType = newEnum()
        .name("ReferType")
        .description("Avenue by which candidate came to the company")
        .value("EMPLOYEE", "Candidate referred by employee(s)")
        .value("EMPLOYEE", "Candidate referred by recruiter")
        .value("ASUCAREER", "Candidate found at ASU Career Fair")
        .value("UACAREER", "Candidate found at U of A Career Fair")
        .build()

val sessionType: GraphQLEnumType = newEnum()
        .name("SessionType")
        .description("Interview type/medium")
        .value("EMPLOYEE", "Candidate referred by recruiter")
        .value("ASUCAREER", "Candidate found at ASU Career Fair")
        .value("UACAREER", "Candidate found at U of A Career Fair")
        .build()