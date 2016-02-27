angular.module("social-document-library")
    .controller("helpController",function($http,$scope,socialDocumentLibraryLocationBaseUrl){
        $scope.adminBookListHelpContent = function(){
            $scope.helpContent = [socialDocumentLibraryLocationBaseUrl,"help/adminBookListHelpContent"].join("/");
        };

        $scope.uploadBookHelpContent = function(){
            $scope.helpContent = [socialDocumentLibraryLocationBaseUrl,"help/uploadBookHelpContent"].join("/");
        }
})
.controller("changeTipShortHelpCtrl",function($scope){
        $scope.tipCode = 0;
        $scope.changeTip = function(tipCode){
            $scope.tipCode = tipCode;
        };

        $scope.nextTip = function(tipNumber){
            console.log($scope.tipCode)
            console.log(tipNumber)

            if($scope.tipCode == tipNumber){
                $scope.tipCode = 0;
            } else{
                ++$scope.tipCode;
            }
            console.log($scope.tipCode)

        };

        $scope.previousTip = function(tipNumber){
            console.log($scope.tipCode)
            console.log(tipNumber)

            if($scope.tipCode == 0){
                $scope.tipCode = tipNumber;
            } else{
                --$scope.tipCode;
            }
            console.log($scope.tipCode)
        }
    });