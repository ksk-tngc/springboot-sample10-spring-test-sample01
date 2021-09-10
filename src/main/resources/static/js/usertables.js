'use strict'

$(function () {
  /**
   * DataTablesを有効化
   */
  $('#user-table').dataTable({
    // DataTablesを日本語化
    language: {
      url: '/webjars/datatables-plugins/i18n/Japanese.json',
    },
    // 各種ボタンを有効化
    dom: 'Bfrtip',
    buttons: ['excelHtml5', 'csvHtml5', 'print'],
  })
})
