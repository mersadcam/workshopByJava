$(function() {

  btn = $(".btn-next-slide")
	btn.click(function() {
		$("#slideshow1").toggleClass("visible");
    $("#slideshow1").toggleClass("invisible");
    $("#slideshow2").addClass("fade-out");

    $("#slideshow2").toggleClass("invisible");
    $("#slideshow2").toggleClass("visible");
    $("#slideshow2").addClass("fade-in");
    
	});
});