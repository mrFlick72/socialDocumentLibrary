$(function(){
    var isLoaded = $.QueryString["isLoaded"];
    if(isLoaded){
        $("#completUploadBookDialog").modal("show");
    }

    var uploadBookFormSubmitButton = document.getElementById("uploadBookFormSubmitButton");
    if(uploadBookFormSubmitButton){
        uploadBookFormSubmitButton.addEventListener("click",function(event){
            event.preventDefault();
            event.stopPropagation();

            document.getElementById("uploadBookForm").submit();
            $("#loadingUploadBookDialog").modal("show");

            return false;
        })
    }

});



