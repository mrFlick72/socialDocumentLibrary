angular.module('social-document-library',['adminBookListModule','notifyModule'])
    .value("socialDocumentLibraryLocationBaseUrl","@admin-documentlibrary-client.serverLocation@")
    .config(function(adminBookListConfigurationServiceProvider){
       adminBookListConfigurationServiceProvider
           .setSearchBookServiceBaseUrl("@search-book-service.searchBookService.baseUrl@")
           .setSearchMetadaTagServiceBaseUrl("@search-book-service.searchMetadaTagService.baseUrl@")
           .setUserInterfaceEndPointBaseUrl("@admin-documentlibrary-client.bookServiceEndPoint.baseUrl@");
    }).controller("rootController",function($window,$scope,$rootScope){
        $scope.backFunction = function(){
            localStorage.back="true";
            $window.history.back();
        };
    });

(function($) {
    $.QueryString = (function(a) {
        if (a == "") return {};
        var b = {};
        for (var i = 0; i < a.length; ++i)
        {
            var p=a[i].split('=');
            if (p.length != 2) continue;
            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
        }
        return b;
    })(window.location.search.substr(1).split('&'))
})(jQuery);

function menuManage(){
    initMenu();
    $( window ).on('resize',  initMenu);

    function initMenu(){
        resetMenuClass();
        if($( document ).width() < 768){
            $("#collaspeMenuBody").addClass("show");
            $("#menuBody").addClass("hide");
            $("#toolBox").addClass("hide");
        }else{
            $("#menuBody").addClass("show");
            $("#collaspeMenuBody").addClass("hide");
            $("#toolBox").addClass("show");
        }
    }
    function resetMenuClass(){
        $("#menuBody").removeClass("hide");
        $("#menuBody").removeClass("show");

        $("#collaspeMenuBody").removeClass("hide");
        $("#collaspeMenuBody").removeClass("show");

        $("#toolBox").removeClass("show");
        $("#toolBox").removeClass("hide");
    }
}

$(function(){
    menuManage();

    $('[data-toggle="tooltip"]').tooltip();

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

   $("[data-select2DataMarker='true']").select2();
   $(":file").filestyle();
});
