/**
 * Created by simoncasey on 16/04/2017.
 */
var m = require("mithril")
var radio = require("radio")

var noopContentProvider = {
  header: function () {},
  body: function () {},
  footer: function () {}
}

var Dialog = {
  dialogContentProvider: noopContentProvider,
  visible: false,
  onclose: function () {
    Dialog.visible = false
    // Dialog.dialogContentProvider.onclose()
    Dialog.dialogContentProvider = Dialog.noopContentProvider
  },
  onopen: function (dialogContentProvider) {
    Dialog.dialogContentProvider = dialogContentProvider
    Dialog.visible = true
  },

  documentHeight: function () {
    let doc = document.documentElement
    return Math.max(doc.clientHeight, window.innerHeight || 0)
  },
  oninit: function (vnode) {
    radio('dialog-open').subscribe(Dialog.onopen)
    radio('dialog-close').subscribe(Dialog.onclose)
  },

  maybeOverlay : function() {
    let smallClose = m('span',
      {onclick: Dialog.onclose},
      m.trust('&times;'))


    let header = m('.modal-header', [
      m('h5.modal-title', Dialog.dialogContentProvider.header()),
      m("button.close[type='button']", [smallClose])
    ])

    var body = m('.modal-body', [
      Dialog.dialogContentProvider.body()
    ])

    var footer = m('.modal-footer', [
      m("button.btn.btn-secondary[type='button']",
        {onclick: Dialog.onclose},
        'Cancel'),
      Dialog.dialogContentProvider.footer()
    ])

    return m(".modal.fade.show.bd-example-modal-lg[style='display: block;'][tabindex='-1']",
      m(".modal-dialog.modal-lg",
        m(".modal-content", [
            header,
            body,
            footer
          ]
        )
      )
    )
  },

  view: function () {
    return Dialog.visible ? Dialog.maybeOverlay() : m('')
  }
}

module.exports = Dialog