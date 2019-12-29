$(function() {
  $(".slideshow").hide();
  $(".slideshow:first").show();
}) ;


$(function() {
  
    $(".btn-next-slide:eq(0)").click(function() {
      toggleSlide(0);
      toggleSlide(1);
    }) ;

    $(".btn-next-slide:eq(1)").click(function() {
      toggleSlide(1);
      toggleSlide(2);
    }) ;

    $(".btn-next-slide:eq(2)").click(function() {
      toggleSlide(2);
      toggleSlide(0);
    }) ;

}) ;


function toggleSlide(slideIndex) {
  $(".slideshow:eq("+slideIndex+")").fadeToggle(300);
}