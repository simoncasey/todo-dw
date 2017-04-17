/**
 * Created by simoncasey on 13/04/2017.
 */
var m = require("mithril")
var radio = require("radio")
var Todo = require("../models/Todo")
var Dialog = require("./Dialog")

var TodoList = {
  oninit: Todo.loadList,
  toModalContent: function (todo) {
    let toUpdate = Object.assign({}, todo);
    let summaryInput = m("input.form-control[id='edit-summary'][type='text'][placeholder='Update summary ...']", {
      oninput: m.withAttr("value", function(value) {toUpdate.summary = value}),
      value: toUpdate.summary
    })
    let descriptionTextArea = m("div", [
      m("label[for='edit-description']",
        "Description"
      ),
      m("textarea.form-control[id='edit-description'][rows='5']", {
        oninput: m.withAttr("value", function(value) {toUpdate.description = value}),
        value: toUpdate.description
      })
    ])
    let saveButton = m("button.btn.btn-primary[type='button']",{
        onclick: function(e) {
          e.preventDefault()
          Todo.update(toUpdate)
            .then(function(result) {
              Dialog.onclose()
              Todo.loadList()
            })
        }},
      "Save changes"
    )

    return {
      header:  function () { return summaryInput },
      body:  function () { return descriptionTextArea },
      footer:  function () { return saveButton },
    }
  },
  view: function () {
    return m("#todo-list", Todo.list.map(function (todo) {
      return m("a.list-group-item.list-group-item-action.flex-column.align-items-start[href='#']",
        [
          m(".d-flex.w-100.justify-content-between",
            [
              m("h5.mb-1",{
                onclick: function() {
                  radio('dialog-open').broadcast(TodoList.toModalContent(todo))
                }
              },
                todo.summary
              ),
              m("small.text-muted",
                todo.status
              )
            ]
          ),
          m("p.mb-1",
            todo.description
          )
        ]
      )
    }))
  }
}

module.exports = TodoList