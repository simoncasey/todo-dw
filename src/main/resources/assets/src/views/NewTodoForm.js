/**
 * Created by simoncasey on 14/04/2017.
 */
var m = require("mithril")
var Todo = require("../models/Todo")

var NewTodoForm = {
  current: {},
  view: function() {
    return m("form", {
      onsubmit: function(e) {
        e.preventDefault()
        Todo.create(NewTodoForm.current)
          .then(function(result) {
            Todo.loadList()
            NewTodoForm.current = {}
          })
      }
    }, [
      m(".form-group.row.d-flex.w-100.justify-content-between", [
        m(".col-10",
          m("input.form-control[id='summary-input'][type='text'][placeholder='Create new Todo here ...']", {
            oninput: m.withAttr("value", function(value) {NewTodoForm.current.summary = value}),
            value: NewTodoForm.current.summary
          })),
        m(".col-2", m("button.btn.btn-primary[type=submit]", "Create")),
        ]
      ),
    ])
  }
}

module.exports = NewTodoForm