angular.module("social-document-library")
    .controller("shortHelpController",function($http,$scope){

        $scope.readBookContentShortHelp = function(){
            $scope.shortHelpContent = "shortHelp/readBookContentHelp";
        };

        $scope.searchContentShortHelp = function(){
            $scope.shortHelpContent = "shortHelp/searchContentHelp";
        };

        $scope.myBookListContentShortHelp = function(){
            $scope.shortHelpContent = "shortHelp/myBookListContentHelp";
        };

        $scope.noContentShortHelp = function(){
            $scope.shortHelpContent = "shortHelp/noContentHelp";
        };

        $scope.initShortHelp = function(){
            $http.get("preference/shortHelp")
                .success(function(data){
                    $scope.noContentShortHelp();
                    $scope.shortHelp = data;
                    $scope.shortHelpUrl = "shortHelp";
                });
        };

        $scope.showShortHelp = function(){
            $http.put("preference/shortHelp/show",{})
                .success(function(data){
                    $scope.noContentShortHelp();
                    $scope.shortHelp = true;
                });
        };

        $scope.hideShortHelp = function(){
            $http.put("preference/shortHelp/hide",{})
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