# Testbed Application
Testbed for new languages, libraries, and features. Initial toolset
consists of Kotlin and GraphQL.

While the purpose of this application is to test and validate various
tools, it is intended as a functional and useful application for
managing the process of interviewing candidates.

## Developer setup
This is a basic RDBMS CRUD application with GraphQL endpoints and Kotlin
backend. It is currently implemented to work against a Postgresql database.

To setup the dev environment, the engineer should install Postgresql locally,
with a database `postgres` and a user `interview` with password `interview`
and all grants against the `postgres` database.

Future refinements may allow for pluggable databases (to support in-memory
DBs for development). Future refinements will support parametrized connection
and authC/Z details.

## Interview Tool MVP

#### Admin functionality

1. Create/update employee records
2. Create/update candidate records
	* Includes school, prior jobs, type of referral, and potential employee referrals
3. Create/update interview sessions - add candidate & interviewers

#### User functionality:

1. Create/update high-level question levels
2. Create/update high-level question categories
3. Create/update questions
4. Write general notes on candidate
5. Select/remove questions for an interview session
6. Leave session notes
7. Leave notes on each question
8. Review previous interview sessions
	* See interviewer notes
	* See questions asked
	* See notes on questions asked

Initial application will provide limited/no support for authC/Z and will not limit access to administrative functions. Future iterations will utilize auth credentials to tie actions to employee/admin performing them, but MVP will require user to select themselves for these actions.

Other features missing from MVP:

* Question filtering, such that questions asked previously can be automatically hidden when selecting new questions
* "Question-on-the-fly" support to create a question from notes on a candidate. Those of us who do not script our interviews often ask questions that are not already written down (generally "soft" topic questions) and it might be a nice feature to select text to convert to a question in that case.
* It is possible that file upload functionality for resumes will not be supported in the MVP. It should come soon after.