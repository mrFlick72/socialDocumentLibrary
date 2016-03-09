angular.module("social-document-library")
    .controller("bookListController",function($scope,$http,adminBookListConfigurationService){
        $scope.filterIfPublished = false;
        $http.get("adminBookList/messages")
                .success(function (data) {
                $scope.messages=data;
                $scope.messages.deleteBookToolTipContent="Se un libro è pubblicato allora non può essere cancellato"
            });

        $http.get(adminBookListConfigurationService.searchMetadaTagServiceBaseUrl)
            .success(function (data) {
                $scope.metadata=data;
            });

        $http.get(adminBookListConfigurationService.userInterfaceEndPointBaseUrl)
            .success(function (data) {
                $scope.bookList = data;
            });

        $scope.$on("setBookListSearchFilterCriteria",function(event,args){
            if(angular.isDefined(args.bookFilterSearchCriteria.onlyNotPublishd)){
                $scope.filterIfPublished=args.bookFilterSearchCriteria.onlyNotPublishd;
            }

            if(angular.isDefined(args.bookFilterSearchCriteria.bookName)){
                $scope.bookNameFilterCriteria=args.bookFilterSearchCriteria.bookName;
            }
        });

        $scope.$on("adminBookList.refreshBookList",function(event,args){
            $http.get(adminBookListConfigurationService.userInterfaceEndPointBaseUrl)
                .success(function (data) {
                    $scope.bookList = data;
                });
        });
    })
    .controller("bookListToolBoxController",function($scope,$rootScope){
        $scope.$watch("bookFilterSearchCriteria.bookName",function(){
            $rootScope.$broadcast("setBookListSearchFilterCriteria",{bookFilterSearchCriteria:$scope.bookFilterSearchCriteria})
        });
        $scope.$watch("bookFilterSearchCriteria.onlyNotPublishd",function(){
            $rootScope.$broadcast("setBookListSearchFilterCriteria",{bookFilterSearchCriteria:$scope.bookFilterSearchCriteria})
        });
    });