angular.module("social-document-library")
    .controller("readBookBreadcrumbController",function($scope){
        $scope.$on("setBookName",function(event,args){
            $scope.bookName=args.bookName;
        });
    })
    .controller("readBookController",function ($scope, $sce, $http, $q, changePageService,notifyPageService){
        var bookIdAux;

        var currentPageKey;

        var previousPageKeyMacro = 37;
        var nextPageKeyMacro = 39;
        var returnKeyMacro = 13;

        function nextPage(){
            var currentPageKeyAux = currentPageKey;
            changePageByPageNumber(++currentPageKeyAux)
        }
        function previousPage(){
            var currentPageKeyAux = currentPageKey;
            changePageByPageNumber(--currentPageKeyAux)
        }

        function changePageByPageNumber(pageNumber){
            var currentPageKeyAux = pageNumber;

            if($scope.pageInfos[currentPageKeyAux]){
                if(!$scope.$$phase){
                    $scope.$apply(function () {
                        $scope.changePage(currentPageKeyAux);
                    });
                } else{
                    $scope.changePage(currentPageKeyAux);
                }
                currentPageKey = currentPageKeyAux;
            }
        }

        $scope.$on("changePage",function(event,args){
            changePageByPageNumber(args.nextPage);
        });

        $scope.$on("nextPage",function(){
            nextPage();
        });

        $scope.$on("previousPage",function(){
            previousPage();
        });

        $scope.$on("enableChangePage",function(event,args){
            if(args.mode=="enabled"){
                $("body").on("keydown",changePageByKeydownManagement);
            } else {
                $("body").off("keydown");
            }
        });

        var changePageByKeydownManagement = function (event) {
            var currentPageKeyAux = currentPageKey;
            if(event.keyCode == nextPageKeyMacro){
                changePageService.nextPage(++currentPageKeyAux);
            }
            if(event.keyCode == previousPageKeyMacro){
                changePageService.previousPage(--currentPageKeyAux);
            }
            return false;
        };

        $("body").on("keydown",changePageByKeydownManagement);

        $scope.initBookReader = function(bookId){
            notifyPageService.setBookId(bookId);
            var currentPageKeyDefer = $q.defer();
            var currentPageKeyPromise = currentPageKeyDefer.promise;

            $http.get(["${user-documentlibrary-client.bookMark.baseUrl}",bookId].join("/"))
                .success(function(data){
                    currentPageKey = data;
                    currentPageKeyDefer.resolve(currentPageKey);
                });

            $http.get(['${book-repository-service.bookServiceEndPoint.baseUrl}',"book",bookId].join('/')+".json?startRecord=-1&pageSize=-1").
                success(function (data) {
                    notifyPageService.setBookName(data.name);
                    $scope.pageInfos = data.pageId;
                    var lastPageNumber = 0;
                    angular.forEach(data.pageId,function(value,key){
                        lastPageNumber++;
                    });
                    changePageService.setLastPage(lastPageNumber);

                    currentPageKeyPromise.then(function(data){
                        changePageByPageNumber(data);
                        notifyPageService.setBookMark(data);
                        $scope.currentPageContent = getPageLink(data);
                    });
                });
        };

       $scope.changePage = function(key){
            $scope.currentPageContent = getPageLink(key);
        };

        function getPageLink(key){
            return $scope.currentLink=$sce.trustAsResourceUrl($scope.pageInfos[key].links[0].href);
        }
    })
    .factory("notifyPageService",function($rootScope){
        return{
            setBookName: function(bookName){
                $rootScope.$broadcast("setBookName",{bookName:bookName})
            },setBookId:function(bookId){
                $rootScope.$broadcast("setBookId",{bookId:bookId});
            },setBookMark:function(bookMark){
                $rootScope.$broadcast("setBookMark",{bookMark:bookMark});
            }
        }
    })
    .factory("changePageService",function($rootScope){
        return {
            changePage:function(nextPage){
                $rootScope.$broadcast("changePage",{nextPage:nextPage})
            },
            nextPage : function(page){
                $rootScope.$broadcast("nextPage",{page:page});
            },
            previousPage : function(page){
                $rootScope.$broadcast("previousPage",{page:page});
            },enableChangePage : function(){
                $rootScope.$broadcast("enableChangePage",{mode:"enabled"});
            },disableChangePage : function(){
                $rootScope.$broadcast("enableChangePage",{mode:"disabled"});
            },setLastPage : function(lastPage){
                $rootScope.$broadcast("setLastPage",{lastPage:lastPage});
            }
        }
    })
    .controller("bookReaderToolBarController",function($scope,$http,changePageService){
        var returnKeyMacro = 13;
        $scope.state = false;
        $scope.$watch("page",function(){
            if($scope.page > $scope.lastPage){
                $scope.page = $scope.lastPage;
            }
            if($scope.page <= 0){
                $scope.page = 1;
            }

            $scope.state = $scope.page == $scope.bookMarkPage;
        });

        $scope.nextPage = function(){
            var pageTest = $scope.page;
            if(changePage(++pageTest,$scope.lastPage)){
                $scope.page++;
            }
        };

        $scope.previousPage = function (){
            var pageTest = $scope.page;
            if(changePage(--pageTest,$scope.lastPage)){
                $scope.page--;
            }
        };

        $scope.enabledBookmark = function(){
            $scope.state = !$scope.state;
            var bookMarkBody = {page:1};

            if($scope.state){
                bookMarkBody.page = $scope.page;
            }

            $http.put(["${user-documentlibrary-client.bookMark.baseUrl}",$scope.bookId].join("/"),bookMarkBody);
        };

        $scope.enableChangePage = function($event){
            if($event.type=="focus"){
                changePageService.disableChangePage();
            } else if($event.type=="blur"){
                changePageService.enableChangePage();
            }
        };

        function changePage(targetPage, lastPage){
            if((targetPage <= lastPage) && (targetPage >= 1)){
                changePageService.changePage(targetPage);
                return true;
            }
            return false;
        }

        $scope.changePage = function($event){
            if($event.keyCode == returnKeyMacro){
                changePage($scope.page,$scope.lastPage);
            }
        };

        $scope.$on("setBookMark",function(event,args){
            $scope.page = args.bookMark;
            $scope.bookMarkPage = args.bookMark;
        });

        $scope.$on("setBookId",function(event,args){
            $scope.bookId = args.bookId;
        });

        $scope.$on("nextPage",function(event,args){
            $scope.page = args.page;
        });

        $scope.$on("previousPage",function(event,args){
            $scope.page = args.page;
        });

        $scope.$on("setLastPage",function(event,args){
            $scope.lastPage = args.lastPage;
        });
    });