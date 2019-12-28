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
    alert("in form#register");
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
      }
    });
    event.preventDefault();
  });

  $( "form#login" ).submit(function( event ) {
    alert("in form#login");
    var json = getFormData($(this));
    var token = localStorage.getItem("token");
    console.log(json);
    $.ajax({
      url: '/login',
      type: "post",
      dataType: 'json',
      headers: {
        "Content-Type": "application/json"
      },
      data: JSON.stringify(json),
      success:function (d) {
        alert("nsn")
        console.log(d);
      }
    });
    event.preventDefault();
  });



});
