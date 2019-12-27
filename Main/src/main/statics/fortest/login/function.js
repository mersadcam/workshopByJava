jQuery(document).ready(function($) {
	tab = $('.tabs h3 a');

	tab.on('click', function(event) {
		event.preventDefault();
		tab.removeClass('active');
		$(this).addClass('active');

		tab_content = $(this).attr('href');
		$('div[id$="tab-content"]').removeClass('active');
		$(tab_content).addClass('active');
	});

  $.ajaxSetup({
    url: "/register",
  });


  $( "form#register" ).submit(function( event ) {
    alert("1")
    var json = $(this).serializeArray();
    console.log(json);

    $.ajax({
      method: 'POST',
      // make sure you respect the same origin policy with this url:
      // http://en.wikipedia.org/wiki/Same_origin_policy
      url: 'http://localhost:8000/register',
      contentType:"application/json",
      dataType:"application/json",
      data: JSON.stringify(json),
      success: function(msg){
        alert('wow' + msg);
      }
    });
    event.preventDefault();
  });



});
