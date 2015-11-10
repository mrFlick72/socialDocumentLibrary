/**
 * Created by Valerio on 27/05/2015.
 */

angular.module("adminBookListModule",['ngSanitize', 'ui.select'])
    .directive("adminBookList",function(){
        return {
            templateUrl : "js/adminBookList/adminBookListTemplate.html",
            restrict : "E",
            scope:{
                    messages:"=messages",
                    bookList:"=books",
                    bookNameFilterCriteria:"=bookNameInListFilter",
                    filterIfPublished:"=filterIfPublished",
                    tags:"=tags"
            },
            compile : function(element,attrs){
                return function($scope,$element,$attr){
                    var select2Object = $element.find("[data-select2DataMarker='true']");
                    angular.element(select2Object).select2();
                }
            },
            controller : function($scope,$rootScope,$http,adminBookListConfigurationService){
                $scope.enableResreshButton = false;

                $scope.openOptionPopUp = function(popupId,item){
                    angular.element(document.getElementById(popupId)).modal("show");
                    $scope.localItem = angular.copy(item);
                    $scope.itemRef = item;
                };

                $scope.confirm = function(){
                    $scope.itemRef.published = $scope.localItem.published;
                    $scope.itemRef.metadata = $scope.localItem.metadata;
                    $scope.itemRef.description = $scope.localItem.description;
                    $scope.itemRef.author = $scope.localItem.author;

                    $http.put([adminBookListConfigurationService.userInterfaceEndPointBaseUrl,$scope.itemRef.bookId].join("/"),$scope.itemRef);
                };

                $scope.unPublishBook = function(book){
                    book.published = false;
                    $scope.changePublishedStatus(book);
                };

                $scope.publishBook = function(book){
                    book.published = true;
                    $scope.changePublishedStatus(book);
                };

                $scope.changePublishedStatus = function(book){
                    var body = {"published":book.published};
                    $http.put([adminBookListConfigurationService.userInterfaceEndPointBaseUrl,book.bookId].join("/"),body);
                };

                $scope.deleteBook = function(book){
                    if(!book.published){
                        $scope.localItem = angular.copy(book);
                        $scope.itemRef = book;
                        angular.element(document.getElementById("confirmDialog")).modal("show");
                    }
                };

                $scope.confirmDelete = function(){
                    $scope.itemRef.delete = true;
                    $http.delete([adminBookListConfigurationService.userInterfaceEndPointBaseUrl,$scope.itemRef.bookId].join("/"));
                };

                $scope.refreshBookList = function(){
                    $rootScope.$broadcast("adminBookList.refreshBookList",{})
                    $scope.enableResreshButton = false;
                };

                $scope.$on("adminBookList.bookChangeStatus.published",function(event,args){
                    angular.forEach($scope.bookList,function(value, key){
                        if(value.bookId === args.bookId){
                            value.published = true;
                        }
                    })
                });

                $scope.$on("adminBookList.bookChangeStatus.delete",function(event,args){
                    angular.forEach($scope.bookList,function(value, key){
                        if(value.bookId === args.bookId){
                            value.delete = true;
                        }
                    })
                });

                $scope.$on("adminBookList.newNotify",function(event,args){
                    $scope.enableResreshButton = true;
                })
            }
        }
    });
