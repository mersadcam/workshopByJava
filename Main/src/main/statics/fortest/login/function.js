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
    var json = $(this).getAttribute("username");
    console.log(json);

    $.ajax({
      url: 'http://localhost:8000/register',
      type: "post",
      dataType: 'json',
      headers: {
        "Content-Type": "application/json"
      },
      data: JSON.stringify({
        username : "ali",
        hashPass : "123dasa"
      }),
      success: function(msg){
        alert('wow' + msg);
      }
    });
    event.preventDefault();
  });



});
