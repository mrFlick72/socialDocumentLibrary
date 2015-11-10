angular.module("social-document-library")
    .controller("shortHelpController",function($http,$scope,socialDocumentLibraryLocationBaseUrl){

        $scope.readBookContentShortHelp = function(){
            $scope.shortHelpContent = [socialDocumentLibraryLocationBaseUrl,"shortHelp/readBookContentHelp"].join("/");
        };

        $scope.searchContentShortHelp = function(){
            $scope.shortHelpContent = [socialDocumentLibraryLocationBaseUrl,"shortHelp/searchContentHelp"].join("/");
        };

        $scope.myBookListContentShortHelp = function(){
            $scope.shortHelpContent = [socialDocumentLibraryLocationBaseUrl,"shortHelp/myBookListContentHelp"].join("/");
        };

        $scope.noContentShortHelp = function(){
            $scope.shortHelpContent = [socialDocumentLibraryLocationBaseUrl,"shortHelp/noContentHelp"].join("/");
        };

        $scope.initShortHelp = function(){
            $http.get([socialDocumentLibraryLocationBaseUrl,"/preference/shortHelp"].join("/"))
                .success(function(data){
                    $scope.noContentShortHelp();
                    $scope.shortHelp = data;
                    $scope.shortHelpUrl = [socialDocumentLibraryLocationBaseUrl,"shortHelp"].join("/");
                });
        };

        $scope.showShortHelp = function(){
            $http.put([socialDocumentLibraryLocationBaseUrl,"/preference/shortHelp/show"].join("/"),{})
                .success(function(data){
                    $scope.noContentShortHelp();
                    $scope.shortHelp = true;
                });
        };

        $scope.hideShortHelp = function(){
            $http.put([socialDocumentLibraryLocationBaseUrl,"/preference/shortHelp/hide"].join("/"),{})
                .success(function(data){
                    $scope.shortHelp = false;
                });
        };

        $scope.$on("showShortHelp",function(){
            $scope.showShortHelp();
        })

})
    .controller("shortHelpMenuController",function($rootScope,$scope){
        $scope.showShortHelp = function(){
            $rootScope.$broadcast("showShortHelp");
        };
}).controller("changeTipShortHelpCtrl",function($scope){
        $scope.tipCode = 0;
        $scope.changeTip = function(tipCode){
            $scope.tipCode = tipCode;
        }

        $scope.nextTip = function(tipNumber){
            if($scope.tipCode == tipNumber){
                $scope.tipCode = 0;
            } else{
                ++$scope.tipCode;
            }
        }

        $scope.previousTip = function(tipNumber){
            if($scope.tipCode == 0){
                $scope.tipCode = tipNumber;
            } else{
                --$scope.tipCode;
            }
        }
    });