jQuery(document).ready(function($) {
  // DATEPICKER
  $('#releaseDate').datepicker({
    // startDate: '-3d',
    format: 'yyyy-mm-dd'
  });

  $('#performed').datepicker({
    // startDate: '-3d',
    endDate: '0d',
    todayHighlight: true,
    format: 'yyyy-mm-dd'
  });


  // TYPEAHEAD
  // Set the Options for "Bloodhound" suggestion engine
  var engine = new Bloodhound({
    remote: {
      url: 'http://localhost:9000/api/track/search?q=%QUERY%',
      wildcard: '%QUERY%'
    },
    datumTokenizer: Bloodhound.tokenizers.whitespace('q'),
    queryTokenizer: Bloodhound.tokenizers.whitespace
  });

  $('#track').typeahead({
    hint: true,
    highlight: true,
    minLength: 2
  }, {
    displayKey: 'name',
    source: engine.ttAdapter()
  });

//
//   $('#track').typeahead({
//       hint: true,
//       highlight: true,
//       minLength: 2
//     }, {
//       source: engine.ttAdapter(),
//
//       name: 'tracksList',
//
//       // the key from the array we want to display (name,id,email,etc...)
//       templates: {
//         empty: [
//           '<div class="dropdown-menu bd-search-results"><div class="dropdown-item">Nothing found.</div></div>'
//         ],
//         header: [
//           '<div class="dropdown-menu bd-search-results">'
//         ],
//         footer: [
//           '</div>'
//         ],
//         suggestion: function (data) {
//           console.log(data);
//           return '<span class="dropdown-item">' + data.fullName + '</span>'
//         }
//       }
//     }
//   );
});
