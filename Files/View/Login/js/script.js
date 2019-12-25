$(function() {
	$(".btn").click(function() {
		$(".form-signin").toggleClass("form-signin-left");
    $(".form-signup").toggleClass("form-signup-left");
    $(".frame").toggleClass("frame-long");

    $("#signin-link").toggleClass("signin-inactive");
    $("#signin-link").toggleClass("signin-active");
    $("#signup-link").toggleClass("signup-inactive");
    $("#signup-link").toggleClass("signup-active");

    $(".forgot").toggleClass("forgot-left");   
    $(this).removeClass("idle").addClass("active");
	});
});

$(function() {
	$(".btn-signup").click(function() {
  $(".nav").toggleClass("nav-up");
  $(".form-signup-left").toggleClass("form-signup-down");
  $(".success").toggleClass("success-left"); 
  $(".frame").toggleClass("frame-short");
	});
});

$(function() {
	$(".btn-signin").click(function() {
  $(".btn-animate").toggleClass("btn-animate-grow");
  $(".frame").toggleClass("frame-close");
  $(".forgot").toggleClass("forgot-fade");
  $(".nav").toggleClass("nav-close");
	});
});