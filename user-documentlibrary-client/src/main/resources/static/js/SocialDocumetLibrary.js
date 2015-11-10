angular.module('social-document-library',['bookListModule'])
    .value("socialDocumentLibraryLocationBaseUrl","${user-documentlibrary-client.server.location}")
    .value("searchMetadaTagServiceBaseUrl","${search-book-service.searchMetadaTagService.baseUrl}")
    .value("userInterfaceBookServiceBaseUrl","${user-documentlibrary-client.bookService.baseUrl}")
    .config(function(bookListConfigurationServiceProvider){
       bookListConfigurationServiceProvider
           .setSearchBookServiceBaseUrl("${search-book-service.searchBookService.baseUrl}")
           .setBookRepositoryServiceBaseUrl("${book-repository-service.bookServiceEndPoint.baseUrl}")
           .setSearchMetadaTagServiceBaseUrl("${search-book-service.searchMetadaTagService.baseUrl}")
           .setUserInterfaceEndPointBaseUrl("${user-documentlibrary-client.bookService.baseUrl}")
           .setUserInterfaceBookReaderEndPointBaseUrl("${user-documentlibrary-client.bookReader.baseUrl}");
    }).controller("rootController",function($window,$scope,$rootScope){
        $scope.backFunction = function(){
            localStorage.back="true";
            $window.history.back();
        };
    });

$(function(){
    $('[data-toggle="tooltip"]').tooltip()

    $("[data-region]").each(function(index,value){
      var valueAux = $(value).data("region");
      var scaleAux;
      switch(valueAux){
         case 'half':
            scaleAux = 0.5;
         break;

         case 'full':
            scaleAux = 1;
         break;
      }
      if(scaleAux){
         $(value).css("min-height",screen.height*scaleAux);
      }
    });

    $("#tags").select2();
    var numericMarker = $("[data-numericMarker='true']");
    numericMarker.number(true, 0, ",", "");
    numericMarker.keyup(function(){
        if($(this).val()=="-0" || $(this).val()=="-0,"){
            $(this).val(1);
        }
   });
});