angular.module('social-document-library')
    .controller("bookUserListPageController", function($scope,$http,
                                                       userInterfaceBookServiceBaseUrl,
                                                       socialDocumentLibraryLocationBaseUrl){

        $scope.searchInList = true;
        $scope.feedbackSectionSetting = {
            feedBackSummit:true,
            feedBackForm:true,
            feedBackSummitIsVisible:false
        };

        function getBookList(){
            $http.get([userInterfaceBookServiceBaseUrl,"bookUserList"].join("/"))
                .success(function (data) {
                    $scope.bookList=data;
                });

        }

        $http.get([socialDocumentLibraryLocationBaseUrl,"bookList/messages"].join("/"))
            .success(function (data) {
                $scope.messages=data;
            });

        $scope.$on("setBookNameSearchCriteria",function(event,args){
            if(args.bookNameSearchCriteria){
                $http.get([userInterfaceBookServiceBaseUrl,"books"].join("/")+"?bookName="+(args.bookNameSearchCriteria || ""))
                    .success(function (data) {
                        $scope.bookList=data;
                    });
            } else{
                getBookList();
            }

        })
    });