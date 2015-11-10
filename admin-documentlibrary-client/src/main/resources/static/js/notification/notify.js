/**
 * Created by Valerio on 16/09/2015.
 */
angular.module("notifyModule",[])
.directive("notify",function(){
        return {
            compile : function(element,attrs){
                document.getElementById("notifyAudioFeedBack").setAttribute("src","@admin-documentlibrary-client.serverLocation@/js/notification/notify.mp3");
            },
            templateUrl : "js/notification/notify.html",
            controller:function($scope,$rootScope){
                $scope.notificationSize;
                window.onload = connect("/@project.build.finalName@/createBookJob");
                function connect(messageRequestUrl) {
                    var socket = new SockJS(messageRequestUrl);
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function(frame) {
                        stompClient.subscribe('/notify/createBookJob/end', function(message){
                            updateNotify(message)
                        });
                        stompClient.subscribe('/notify/createBookJob/new', function(message){
                            updateNewNotify()
                        });
                    });
                }

                function updateNewNotify(){
                    $("#notify").addClass("newNotify");
                    setTimeout(function(){
                        $("#notify").addClass("newNotify-leave");
                    },1000);
                    $rootScope.$broadcast("adminBookList.newNotify",{});
                    document.getElementById("notifyAudioFeedBack").play();
                }


                function updateNotify(message){
                    $("#notifyCommand").addClass("enable-notify");
                    var messageBody = JSON.parse(message.body);

                    if($scope.notificationSize)
                        if(messageBody.length > 0){
                            $rootScope.$broadcast("enableNotify",{enableNotify:true});
                        } else{
                            $rootScope.$broadcast("enableNotify",{enableNotify:false});
                        }

                    $rootScope.$broadcast("notifyData",{data:messageBody});
                    $scope.notificationSize=messageBody.length;
                }

                $scope.notifyContainer = {isInvisible:true,isVisible:false};

                $scope.showNofityContainer = function(){
                    $("#notify").removeClass("newNotify");

                    $rootScope.$broadcast("toggleNotifyDialogContainer",{
                        isInvisible:!$scope.notifyContainer.isInvisible,
                        isVisible:!$scope.notifyContainer.isVisible
                    });
                };

                $scope.$on("enableNotify",function(event,args){
                    $scope.notificationSize=args.notificationSize;

                    if(args.enableNotify === true){
                        $("#notifyCommand").addClass("enable-notify");
                    } else {
                        $("#notifyCommand").removeClass("enable-notify");
                    }
                });
            }
        }
    })
.directive("notifyContainer",function(){
    return {
        templateUrl : "js/notification/notify-container.html",
        controller:function($scope, $rootScope, $http){
            $scope.notifyContainer = {isInvisible:true,isVisible:false};
                $http.get("http://localhost:7070/@project.build.finalName@/historyNotifyEntity/messages")
                    .success(function(data){
                        $scope.messages  = data;
                    });

            $http.get("http://localhost:7070/@project.build.finalName@/historyNotifyEntity")
                .success(function(data){
                    $scope.notifyData = data;
                    sendEnableNotifyService(data);
                });

            $scope.showNofityContainer = function(){
                $scope.notifyContainer.isInvisible = !$scope.notifyContainer.isInvisible;
                $scope.notifyContainer.isVisible = !$scope.notifyContainer.isVisible;
            };

            $scope.publishBookByNotify = function(item){
                $http.get(["@search-book-service.searchBookService.baseUrl@",item.bookId].join("/"))
                    .success(function(data){
                        if(data.published){
                            $scope.errorMessage = "Sorry but the book is already published. The notification will be removed";
                            $scope.localCallable = true;
                            $("#notifyErrorDialog").modal("show");
                        } else{
                            $rootScope.$broadcast("adminBookList.bookChangeStatus.published",{bookId:item.bookId});
                        }
                    })
                    .error(function(){
                        $scope.localItem = item;
                        $scope.errorMessage = "Sorry but the notification is not valid. The operation cannot be performed.The notification will be removed";
                        $scope.localCallable = true;
                        $("#notifyErrorDialog").modal("show");
                    });


                $http.put("http://localhost:7070/@project.build.finalName@/historyNotifyEntity/"+item.bookId+"/pubishBook")
                    .success(function(){
                        $rootScope.$broadcast("adminBookList.bookChangeStatus.published",{bookId:item.bookId})
                    })
                    .error(function(){
                        $scope.localItem = item;
                        $scope.errorMessage = "Sorry but the notification is not valid. The operation cannot be performed.The notification will be removed";
                        $scope.localCallable = true;
                        $("#notifyErrorDialog").modal("show");
                    })};

            $scope.deleteBookByNotify = function(item){
                $http.get(["@search-book-service.searchBookService.baseUrl@",item.bookId].join("/"))
                    .success(function(){
                        $http.delete("http://localhost:7070/@project.build.finalName@/historyNotifyEntity/"+item.bookId+"/deleteBook")
                            .success(function(){
                                $rootScope.$broadcast("adminBookList.bookChangeStatus.delete",{bookId:item.bookId});
                            })
                            .error(function(){
                                $scope.errorMessage = "Sorry but for delete a book,it must not be published";
                                $scope.localCallable = true;
                                $("#notifyErrorDialog").modal("show");
                            }
                        )
                    })
                    .error(function(){
                        $scope.errorMessage = "Sorry but the notification is not valid. The operation cannot be performed.The notification will be removed";
                        $scope.localCallable = true;
                        $("#notifyErrorDialog").modal("show");
                    })
            };

            $scope.openDeleteBookByNotifyPopUp = function(item){
                $scope.localItem = item;
                $("#deleteBookNotifyDialog").modal("show");
            };

            $scope.deleteNotify = function(item,callable){
                if(callable){
                    $http.delete("http://localhost:7070/@project.build.finalName@/historyNotifyEntity/"+item.bookId);
                }
            };

            $scope.$watch("notifyData",function(){
                sendEnableNotifyService($scope.notifyData);
            });

            $scope.$on("toggleNotifyDialogContainer",function(event,attrs){
                $scope.notifyContainer = {
                    isInvisible:attrs.isInvisible,
                    isVisible:attrs.isVisible
                };
            });

            $scope.$on("notifyData",function(event,attrs){
                $scope.$apply(function(){
                    $scope.notifyData = attrs.data;
                })
            });

            function sendEnableNotifyService(data){
                if(data.length > 0){
                    $rootScope.$broadcast("enableNotify",{enableNotify:true,notificationSize:data.length});
                } else{
                    $rootScope.$broadcast("enableNotify",{enableNotify:false,notificationSize:data.length});
                }
            }
        }
    }
});






