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


  function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
      indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
  }


  $( "form#register" ).submit(function( event ) {

    var json = getFormData($(this));

    $.ajax({
      url: '/register',
      type: "post",
      dataType: 'json',
      headers: {
        "Content-Type": "application/json"
      },
      data: JSON.stringify(json),
      success:function (d) {
        localStorage.setItem("token",d["token"]);
        location.href = "/dashboard";
      }
    });
    event.preventDefault();
  });

  $( "form#login" ).submit(function( event ) {

    var json = getFormData($(this));

    $.ajax({
      url: '/login',
      type: "post",
      dataType: 'json',
      headers: {
        "Content-Type": "application/json"
      },
      data: JSON.stringify(json),
      success:function (d) {
        localStorage.setItem("token",d["token"]);

        $.get({

          headers: {
            "token": localStorage.getItem("token")
          },
          url:"/dashboard",
          type:"get"

        })

      }
    });
    event.preventDefault();
  });



});
