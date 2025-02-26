package routes

import com.stackmobile.data.model.ErrorResponse
import com.stackmobile.data.model.Expense
import com.stackmobile.data.model.expenses
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.expensesRouting() {

    get("/expenses") {
        if(expenses.isEmpty()) {
            call.respondText {"No expenses found"}
        } else {
            call.respond(status = HttpStatusCode.OK, expenses)
        }
    }

    get("/expenses/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if(id == null || id  !in 0 until expenses.size) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Expense not found"))
        }
        else{
            call.respond(HttpStatusCode.OK, expenses[id.toInt()])
        }
    }

    post("/expenses") {
        val expense = call.receive<Expense>().copy(id = expenses.size.toLong() + 1)
        expenses.add(expense)
        call.respond(HttpStatusCode.OK, "Expense added successfully")
    }
    put("expenses/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val expense = call.receive<Expense>()
        if(id == null || id  !in 0 until expenses.size) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Expense not found"))
        }
        else{
            val index = expenses.indexOfFirst { it.id == id }
            expenses[index] = expense.copy(id = id)
            call.respond(HttpStatusCode.OK, expenses[id.toInt()])
        }
    }
}