angular.module('social-document-library')
    .controller("searchBookPageController", function($scope,$http,$sce,$window,
                                                     userInterfaceBookServiceBaseUrl,
                                                     searchMetadaTagServiceBaseUrl,
                                                     socialDocumentLibraryLocationBaseUrl){
        $scope.searchInList = false;

        $http.get("bookList/messages")
            .success(function (data) {
                $scope.messages=data;
            });

        $http.get(searchMetadaTagServiceBaseUrl)
            .success(function (data) {
                $scope.searchBookTags=data;
            });

        $scope.messages = {
            listHeader : "List of books found",
            openBookLabel :"Read Book"
        };

        $scope.feedbackSectionSetting = {
            feedBackForm:false,
            feedBackSummit:true,
            feedBackSummitIsVisible:true
        };

        $scope.searchBooks = function(searchBookCriteria){
            if(angular.isDefined(searchBookCriteria)){
                var queryUrl = "bookName=";

                if(angular.isDefined(searchBookCriteria.bookName)){
                    queryUrl+=searchBookCriteria.bookName;
                }
                queryUrl+="&searchTags=";

                var index = 0;
                angular.forEach(searchBookCriteria.tags,function(value,key){
                    queryUrl+=value;
                    if(index < searchBookCriteria.tags.length-1){
                        queryUrl+=",";
                    }
                    index++;
                });
                $http.get([userInterfaceBookServiceBaseUrl,queryUrl].join("?"))
                    .success(function (data) {
                        $scope.bookList=data;
                        if($scope.bookList.length==0){
                            $("#searchFeedBackModalDialog").modal('show');
                        }
                    }).error(function(){
                        $("#searchFeedBackModalDialog").modal('show');
                    });
            } else {
                $("#searchFeedBackModalDialog").modal('show');
            }
        };
    });