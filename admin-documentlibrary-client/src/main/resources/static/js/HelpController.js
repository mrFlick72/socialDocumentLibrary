angular.module("social-document-library")
    .controller("helpController",function($http,$scope,socialDocumentLibraryLocationBaseUrl){
        $scope.adminBookListHelpContent = function(){
            $scope.helpContent = "help/adminBookListHelpContent";
        };

        $scope.uploadBookHelpContent = function(){
            $scope.helpContent = "help/uploadBookHelpContent";
        }
})
.controller("changeTipShortHelpCtrl",function($scope){
        $scope.tipCode = 0;
        $scope.changeTip = function(tipCode){
            $scope.tipCode = tipCode;
        };

        $scope.nextTip = function(tipNumber){
            if($scope.tipCode == tipNumber){
                $scope.tipCode = 0;
            } else{
                ++$scope.tipCode;
            }
        };

        $scope.previousTip = function(tipNumber){
            if($scope.tipCode == 0){
                $scope.tipCode = tipNumber;
            } else{
                --$scope.tipCode;
            }
        }
    });