/**
 * Created by simoncasey on 13/04/2017.
 */
// index.js
var m = require("mithril")

var TodoList = require("./views/TodoList")
var NewTodoForm = require("./views/NewTodoForm")
var Dialog = require("./views/Dialog")

var Page = {
  view: function() {
    return m(".container", [
      m(Dialog),
      m("h1", "Todos"),
      m(NewTodoForm),
      m(TodoList)
      ])
  }
}

m.mount(document.body, Page)