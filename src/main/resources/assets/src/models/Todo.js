/**
 * Created by simoncasey on 13/04/2017.
 */
var m = require("mithril")

var Todo = {
  list: [],
  loadList: function() {
    return m.request({
      method: "GET",
      url: "http://localhost:8080/api/todo"
    })
      .then(function(result) {
        Todo.list = result
      })
  },

  current: {},
  load: function(id) {
    return m.request({
      method: "GET",
      url: "http://localhost:8080/api/todo/:id",
      data: {id: id},
    })
      .then(function(result) {
        Todo.current = result
      })
  },

  create: function(newTodo) {
    return m.request({
      method: "POST",
      url: "http://localhost:8080/api/todo",
      data: newTodo,
    })
  },

  update: function(todo) {
    return m.request({
      method: "PUT",
      url: "http://localhost:8080/api/todo/" + todo.id,
      data: todo,
    })
  },
}

module.exports = Todo