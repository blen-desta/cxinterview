package com.connexta.interview.data

import com.connexta.db.artifact.tables.Employee.EMPLOYEE

data class Employee(override val id: Int, var username: String) : IdObject

fun Employee.list(): List<Employee> {
    return useConnection { ctx ->
        ctx.selectFrom(EMPLOYEE)
                .fetch {
                    Employee(it.id, it.username)
                }
    }
}

fun Employee.updateOrCreate(): Employee {
    return useConnection { ctx ->
        if (this.isNew()) {
            val record = ctx.newRecord(EMPLOYEE)
            with(record) {
                username = this@updateOrCreate.username
                store()
            }
            this.copy(id = record.id)
        } else {
            ctx.update(EMPLOYEE)
                    .set(EMPLOYEE.USERNAME, this@updateOrCreate.username)
                    .where(EMPLOYEE.ID.eq(this@updateOrCreate.id))
            this
        }
    }
}

fun Employee.delete() {
    useConnection { ctx ->
        ctx.deleteFrom(EMPLOYEE)
                .where(EMPLOYEE.ID.eq(this.id))
    }
}
