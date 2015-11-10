$(function(){
    var isLoaded = $.QueryString["isLoaded"];
    if(isLoaded){
        $("#completUploadBookDialog").modal("show");
    }

    console.log("uploadBookFormSubmitButton: " + document.getElementById("uploadBookFormSubmitButton"))

    document.getElementById("uploadBookFormSubmitButton").addEventListener("click",function(event){
        event.preventDefault();
        event.stopPropagation();

        document.getElementById("uploadBookForm").submit();
        $("#loadingUploadBookDialog").modal("show");

        return false;
    })
});



