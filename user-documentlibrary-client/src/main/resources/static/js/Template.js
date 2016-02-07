/**
 * Created by Valerio on 06/02/2016.
 */

$(function(){
    initMenu();
    $( window ).on('resize',  initMenu);

    function initMenu(){
        if($( document ).width() < 768){
            console.log("under 768")
            resetClass()
            $("#collaspeMenuBody").addClass("show");
            $("#menuBody").addClass("hide");
        }else{
            console.log("over 768")
            resetClass()
            $("#menuBody").addClass("show");
            $("#collaspeMenuBody").addClass("hide");
        }
    }
    function resetClass(){
        $("#menuBody").removeClass("hide");
        $("#menuBody").removeClass("show");

        $("#collaspeMenuBody").removeClass("hide");
        $("#collaspeMenuBody").removeClass("show");
    }
});