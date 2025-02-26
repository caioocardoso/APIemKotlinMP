package routes

import com.stackmobile.data.model.ErrorResponse
import com.stackmobile.data.model.Expense
import com.stackmobile.data.model.expenses
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.exp

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
        val expense = expenses.find {it.id == id}
        if(id == null || expense == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Expense not found"))
        }
        else{
            call.respond(HttpStatusCode.OK, expense)
        }
    }

    post("/expenses") {
        val expense = call.receive<Expense>()
        val maxId = expenses.maxOf {it.id} + 1
        expenses.add(expense.copy(id = maxId))
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

    delete("expenses/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val expense = expenses.find {it.id == id}
        if(id == null || expense == null ) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Expense not found"))
        }
        else{
            expenses.removeIf { it.id == id}
            call.respond(HttpStatusCode.OK, "Expense removed successfully")
        }
    }
}