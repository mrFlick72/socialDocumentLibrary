angular.module("social-document-library")
    .controller("homeController",function($scope,$http,adminBookListConfigurationService){
        $http.get("adminBookList/messages")
            .success(function (data) {
                $scope.messages=data;
            });

        $http.get(adminBookListConfigurationService.searchMetadaTagServiceBaseUrl)
            .success(function (data) {
                $scope.metadata=data;
            });

        $http.get(adminBookListConfigurationService.userInterfaceEndPointBaseUrl)
            .success(function (data) {
                $scope.bookList = data;
            });
    });