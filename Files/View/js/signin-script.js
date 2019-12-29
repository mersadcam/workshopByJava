$(function() {
  $(".signin-signup-frame").hide();
  $(".form-signup").hide();
}) ;

$(function() {
	$(".signin-signup-btn").click(function() {
    $(".signin-signup-frame").fadeToggle();
    $(".slideshow-container").fadeTo("fast",0.3);
    $(".navbar").fadeTo("fast",0.3);
  });
  
  $(".btn-close").click(function() {
    $(".signin-signup-frame").fadeToggle()
    $(".slideshow-container").fadeTo("fast",1);
    $(".navbar").fadeTo("fast",1);
	});
});


$(function() {
	$(".signin-signup-switch").click(function() {
		$(".form-signin").fadeToggle();
    $(".form-signup").fadeToggle();
    $("#signin-switch").toggleClass("signin-signup-switch-active");
    $("#signin-switch").toggleClass("signin-signup-switch-inactive");
    $("#signup-switch").toggleClass("signin-signup-switch-active");
    $("#signup-switch").toggleClass("signin-signup-switch-inactive");
	});
});