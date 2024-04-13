import de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.api.action.htmx.SwapType
import de.allround.kotlinweb.api.action.trigger.DomEvents
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.components.misc.InputType
import de.allround.kotlinweb.api.components.misc.TextType
import de.allround.kotlinweb.api.div
import de.allround.kotlinweb.api.misc.DebugLogger
import de.allround.kotlinweb.api.page
import de.allround.kotlinweb.api.page.Page
import de.allround.kotlinweb.api.rest.DELETE
import de.allround.kotlinweb.api.rest.GET
import de.allround.kotlinweb.api.rest.POST
import de.allround.kotlinweb.api.fragment
import io.vertx.ext.web.RequestBody
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.Session
import java.util.*
import kotlin.time.Duration.Companion.milliseconds


data class Todo(val id: String, val content: String, var done: Boolean)

object TodoApp {
    @GET("/")
    fun getTodoApp(session: Session): Page = page {
        text(type = TextType.H1, text = "Todos")
        text(type = TextType.H3, text = "Simply track your todos with the list below...")

        linebreak()

        val todoList = TodoAppComponents.TodoList(TodoAppDataManager.getTodos(session))
        addChild(todoList)

        linebreak()

        div {
            input(type = InputType.TEXT, name = "todo_content", placeholder = "Enter new todo...") {
                classes.add("new-todo-input")
            }
            button(text = "Add Todo") {
                on(Trigger.DomEvent(DomEvents.CLICK)) {
                    wait(20.milliseconds) {
                        setValue("", target = "the closest <.new-todo-input />")
                    }
                    post("/api/todo/add") {
                        include.add("div > input.new-todo-input")
                        swapType = SwapType.beforeend
                        target = "#todo-list"
                    }
                }
            }
        }
    }

    @POST("/api/todo/update/:id")
    fun postUpdateTodoById(body: RequestBody, session: Session, context: RoutingContext): Component {
        val todoId = context.pathParam("id")
        val todo = TodoAppDataManager.getTodos(session).find { it.id == todoId } ?: return div {
            classes.add("toast_error")
            text(text = "Todo not found!") {
                classes.add("toast_msg_error")
            }
        }
        todo.done = !todo.done
        return div {
            classes.add("toast_success")
            text(text = "Todo updated!") {
                classes.add("toast_msg_success")
            }
        }
    }

    @POST("/api/todo/add")
    fun postAddTodo(body: RequestBody, session: Session): Component {
        val todoContent = body.asJsonObject().getString("todo_content")
        if (todoContent.isEmpty()) return fragment {}
        val todo = TodoAppDataManager.addTodo(session, todoContent)
        return TodoAppComponents.TodoListItem(todo)
    }

    @GET("/api/todo_list")
    fun getTodoList(session: Session): TodoAppComponents.TodoList =
        TodoAppComponents.TodoList(TodoAppDataManager.getTodos(session))

    @DELETE("/api/todo/:id")
    fun deleteTodoById(session: Session, context: RoutingContext): Component {
        val todoId = context.pathParam("id")
        TodoAppDataManager.removeTodoById(session, todoId)
        return div {
            classes.add("toast_success")
            text(text = "Todo removed!") {
                classes.add("toast_msg_success")
            }
        }
    }
}

object TodoAppDataManager {
    fun getTodos(session: Session): MutableList<Todo> {
        session.putIfAbsent("todos", mutableListOf<Todo>())
        return session.get("todos")
    }

    fun addTodo(session: Session, content: String): Todo {
        val id = UUID.randomUUID().toString().replace("-", "")
        val todo = Todo(id = id, content = content, done = false)
        getTodos(session).add(todo)
        return todo
    }

    fun removeTodoById(session: Session, id: String) {
        getTodos(session).removeIf {
            it.id == id
        }
    }
}

object TodoAppComponents {
    class TodoListItem(todo: Todo) : Component(type = "li", onInit = {
        text(type = TextType.SPAN, text = todo.content)
        input(type = InputType.CHECKBOX, name = "is_done") {
            id = UUID.randomUUID().toString()
            if (todo.done) attributes["checked"] = ""
            on(Trigger.DomEvent(DomEvents.CLICK)) {
                post("/api/todo/update/${todo.id}") {
                    swapType = SwapType.none
                }
            }
        }
        button(text = "Remove") {
            on(Trigger.DomEvent(DomEvents.CLICK)) {
                remove("the closest <li />")
                delete("/api/todo/${todo.id}") {
                    swapType = SwapType.none
                }
            }
        }
    })


    class TodoList(entries: List<Todo>) : Component(type = "ul", id = "todo-list", onInit = {
        entries.forEach {
            addChild(TodoListItem(it))
        }
    })
}

fun main() {
    DebugLogger.isEnabled = true
    val webApplication = WebApplication(
        endpoints = mutableListOf(
            TodoApp
        )
    )

    webApplication.start()
}