/**
 * Created by simoncasey on 13/04/2017.
 */
var m = require("mithril")
var radio = require("radio")
var Todo = require("../models/Todo")
var Dialog = require("./Dialog")

const STATUS_DELETED = "Deleted";
const STATUS_INCOMPLETE = "Incomplete";
const STATUS_COMPLETE = "Complete";

var isComplete = function (todo) {
  return todo.status === STATUS_COMPLETE
}

var markChecked = function (todo, fn) {
  if (fn(todo)) return "[checked='checked']"
  return ""
}

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

    return m("#todo-list.list-group", Todo.list.sort((a, b) => b.priority - a.priority).map(function (todo) {
      return m("a.list-group-item.list-group-item-action.flex-column.align-items-start[href='#']",
          m(".d-flex.w-100.justify-content-between",
            [
              m("i.fi-arrow-up[style='padding-right: 10px']", {
                onclick: function (e) {
                  e.preventDefault()
                  todo.priority++
                  Todo.update(todo).then(function() {
                    Todo.loadList();
                  })
                }
              }, ""),
              m("i.fi-arrow-down[style='padding-right: 10px']", {
                onclick: function (e) {
                  e.preventDefault()
                  todo.priority--
                  Todo.update(todo).then(function() {
                    Todo.loadList();
                  })
                }
              }, ""),
              m("h5.mb-1.w-100", {
                  onclick: function() {
                    radio('dialog-open').broadcast(TodoList.toModalContent(todo))
                  }
                },
                todo.summary
              ),
              m(".form-check",
                m(".form-check-label", [
                  m("small.text-muted[style='margin-right:30px;']", todo.status),
                  m("input.form-check-input[type='checkbox'][id='status-checkbox']" + markChecked(todo, isComplete), {
                    onclick: m.withAttr("checked", function(selected) {
                      todo.status = selected ? STATUS_COMPLETE : STATUS_INCOMPLETE
                      Todo.update(todo).then(function(result) {
                        todo = result
                      })
                    })
                  })
                ])
              )
            ]
          )
    )}))
  }
}

module.exports = TodoList