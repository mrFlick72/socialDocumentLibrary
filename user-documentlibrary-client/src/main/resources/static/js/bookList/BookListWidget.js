angular.module("bookListModule",['ngSanitize'])
            .directive("bookList",function(){
        return {
            templateUrl : "js/bookList/bookListTemplate.html",
            restrict : "E",
            scope :{
                        readUserFeedBack:"=readUserFeedBack",
                        isRemovable:"=isRemovable",
                        feedbackSectionSetting:"=feedbackSectionSetting",
                        messages:"=messages",
                        bookList : "=bookList",
                        searchInList:"=searchInList"
            },
            controller: function($rootScope,$scope,$sce,$http,$sanitize,bookListConfigurationService,forvardScoreValueService){
                $scope.$watch("bookNameSearchCriteria",function(){
                    $rootScope.$broadcast("setBookNameSearchCriteria",{bookNameSearchCriteria:$scope.bookNameSearchCriteria})
                });

                $scope.scoresToArray = function(scoreNumber){
                    var result = [];
                    for(var index = 0 ; index < scoreNumber ; index++){
                        result.push(index);
                    }
                    return result;
                };

                $scope.$watch("bookList",function(){
                    var i = 0;
                    if(!angular.isDefined($scope.bookList) || $scope.bookList.length == 0){
                        $scope.noBookMessage = "No book in list"
                    } else{
                        $scope.noBookMessage = undefined;
                        if(i==0){
                            angular.forEach($scope.bookList,function(value,key){
                                value.feedBackSummitIsVisible = $scope.feedbackSectionSetting.feedBackSummitIsVisible;
                            });
                            i++;
                        }
                    }
                });

                var scoresAux;

                $scope.changeFeedBackSummitIsVisible = function(book){
                    book.feedBackSummitIsVisible = !book.feedBackSummitIsVisible;
                };

                $scope.openConfirmRemoveBookFromUserBookListPopUp = function(book){
                    angular.element(document.getElementById("deleteBookConfirmDialog")).modal("show");
                    $scope.itemRef4ConfirmRemoveFromBookList = book;
                };

                $scope.removeBookFromUserBookList = function(){
                    var book = $scope.itemRef4ConfirmRemoveFromBookList;
                    $http.delete([bookListConfigurationService.userInterfaceEndPointBaseUrl,"bookUserList",book.bookId].join("/"))
                        .success(function(data){
                            var index = $scope.bookList.indexOf(book);
                            if (index > -1) {
                                $scope.bookList.splice(index, 1);
                            }
                            if($scope.bookList.length == 0){
                                $scope.noBookMessage = "No book in list"
                            }
                        });
                };

                $scope.readBookUrlFactory = function(bookId,bookName){
                    return $sce.trustAsResourceUrl(bookListConfigurationService.userInterfaceBookReaderEndPointBaseUrl+"?bookId="+ bookId+"&bookName="+bookName);
                };

                $scope.openFeedBackDialog = function(item){
                    $scope.itemRef = item;
                    $scope.localItem = angular.copy(item);
                    angular.element(document.getElementById("commentEditDialog")).modal("show");
                };

                $scope.confirm = function(){
                    $scope.itemRef.feedback.feadbackTitle = $scope.localItem.feedback.feadbackTitle;
                    $scope.itemRef.feedback.feadbackBody = $scope.localItem.feedback.feadbackBody;
                    $scope.itemRef.feedback.score = $scope.localItem.feedback.score;

                    var body = {
                                "bookId": $scope.itemRef.bookId,
                                "feadbackTitle": $scope.localItem.feedback.feadbackTitle,
                                "feadbackBody": $scope.localItem.feedback.feadbackBody,
                                "score": $scope.localItem.feedback.score
                            };

                    if($scope.itemRef.feedback.id){
                        $http.put([bookListConfigurationService.userInterfaceEndPointBaseUrl,"feedBack",$scope.itemRef.feedback.id].join("/"),body);
                    } else{
                        $http.post([bookListConfigurationService.userInterfaceEndPointBaseUrl,"feedBack"].join("/"),body).
                            success(function(data, status, headers, config) {
                                var urlSplitted = headers("Location").split("/");
                                var feedBackId = urlSplitted[urlSplitted.length-1].split("?")[0];
                                $scope.itemRef.feedback.id=feedBackId;
                            })
                    }

                    forvardScoreValueService.updateScore(scoresAux);
                };

                $scope.$on("forwardScore",function(event,args){
                    var oldScore = $scope.localItem.feedback.scores[args.oldScore];
                    var newScore = $scope.localItem.feedback.scores[args.newScore];

                    $scope.localItem.feedback.scores[args.oldScore] = forvardScoreValueService.calculateNewScores(oldScore,-1);
                    $scope.localItem.feedback.scores[args.newScore] = forvardScoreValueService.calculateNewScores(newScore,1);

                    scoresAux = $scope.localItem.feedback.scores;
                });
            }
        }
    })
    .directive("contenteditable", function() {
        return {
            restrict: "A",
            require: "ngModel",
            link: function(scope, element, attrs, ngModel) {

                function read() {
                    ngModel.$setViewValue(element.html());
                }

                ngModel.$render = function() {
                    element.html(ngModel.$viewValue || "");
                };

                element.bind("blur keyup change", function() {
                    scope.$apply(read);
                });
            }
        };
    })
    .directive("scoreInput", function(forvardScoreValueService) {
        return {
            restrict: "E",
            require:"ngModel",
            scope: {readOnly:"=readOnly"},
            templateUrl:"js/bookList/scoreInputTemplate.html",
            link: function(scope, element, attrs,ngModel) {
                var scoresItems = angular.element(element).find("i");

                var oldValue;

                ngModel.$render = function() {
                    oldValue = ngModel.$viewValue;
                    setScore(ngModel.$viewValue);
                };

                function setScore(dataScore){
                    angular.forEach(scoresItems,function(value, key) {
                            angular.element(value)
                                .removeClass("icon-star")
                                .addClass("icon-star-empty");
                        }
                    );
                    var find = false;

                    if(dataScore){
                        angular.forEach(scoresItems,function(value, key) {
                                if(!find){
                                    value.className = "icon-star";
                                }

                                if(angular.equals(value.dataset.score,dataScore)){
                                    find = true;
                                }
                            }
                        );
                    }

                    ngModel.$setViewValue(dataScore);
                }

                scope.setScore = function($event){
                    if(!scope.readOnly){
                        var dataScore = angular.element($event.target).attr("data-score");
                        setScore(dataScore);

                        forvardScoreValueService.forwardScore(oldValue,dataScore,"scoreReader");
                        oldValue = ngModel.$viewValue;
                    }
                };
            }
        };
    })
    .directive("scoreReader", function() {
        return {
            restrict: "E",
            scope:{score:"=scores"},
            templateUrl:"js/bookList/scoreReaderTemplate.html",
            link: function(scope, element, attrs) {
                var PART_INT_INDEX = 0;
                var MANTISSA_INDEX = 1;

                var calulateScore = function(){
                    if(angular.isDefined(scope.score)){
                        scope.oneStarScore = scope.score["1"] || "0";
                        scope.twoStarScore = scope.score["2"] || "0";
                        scope.threeStarScore = scope.score["3"] || "0";
                        scope.fourStarScore = scope.score["4"] || "0";
                        scope.fiveStarScore = scope.score["5"] || "0";

                        scope.average = scoreAverage(scope.score);
                    }
                    setAverageScore(scope.average);
                };

                calulateScore();

                function setAverageScore(averageScore){
                    if(averageScore) {
                        var averageScoresItems= element.find("[data-average-row=true]").find("i");

                        angular.forEach(averageScoresItems,function(value, key) {
                                value.className = "icon-star-empty";
                        });

                        var find = false;
                        var mantissaAndIntPart = getMantissaAndIntPart(averageScore);

                        angular.forEach(averageScoresItems,function(value, key) {
                            if(key < mantissaAndIntPart[PART_INT_INDEX]){
                                value.className = "icon-star";
                            }

                            if((key == mantissaAndIntPart[PART_INT_INDEX]) && mantissaAndIntPart[MANTISSA_INDEX]){
                                value.className = "icon-star-half-alt";
                            }
                        });
                    }
                }

                function scoreAverage(scoreAux){
                    var numerator   = 0;
                    var denominator = 0;

                    angular.forEach(scoreAux,function(value, key){
                        numerator+=key*value;
                        denominator+=Number(value);
                    });

                    var average = denominator!=0 ? numerator/denominator : 0;
                    if(getMantissaAndIntPart(average)[MANTISSA_INDEX] != 0){
                        average = average.toFixed(1);
                    }

                    return average;
                }

                function getMantissaAndIntPart(average){
                    var intPart = Math.floor(average);
                    var mantissa = average - intPart;
                    var result = [];

                    result[PART_INT_INDEX] = intPart;
                    result[MANTISSA_INDEX] = mantissa;

                    return result;
                }

                scope.$on("updateScore",function(event,args){
                    scope.score=args.newScores;
                    calulateScore();
                })
            }
        };
    })
    .factory("forvardScoreValueService",function($rootScope){
        return {
            calculateNewScores:function(score,delta){
                if(score){
                    return Number(score) + Number(delta) < 1 ? Number(score) + Number(delta) : 1;
                } else{
                    return Number(delta);
                }
            },
            forwardScore:function(oldScore,newScore,reciver){
                $rootScope.$broadcast("forwardScore",
                    {
                        oldScore:oldScore,
                        newScore:newScore,
                        reciver:reciver
                    })
            },
            updateScore:function(newScores){
                $rootScope.$broadcast("updateScore",{newScores:newScores});
            }
        }
    });