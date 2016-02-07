/**
 * Created by Valerio on 06/02/2016.
 */

$(function(){
    initMenu();
    $( window ).on('resize',  initMenu);

    function initMenu(){
        resetMenuClass()
        if($( document ).width() < 768){
            $("#collaspeMenuBody").addClass("show");
            $("#menuBody").addClass("hide");
        }else{
            $("#menuBody").addClass("show");
            $("#collaspeMenuBody").addClass("hide");
        }
    }
    function resetMenuClass(){
        $("#menuBody").removeClass("hide");
        $("#menuBody").removeClass("show");

        $("#collaspeMenuBody").removeClass("hide");
        $("#collaspeMenuBody").removeClass("show");
    }
});