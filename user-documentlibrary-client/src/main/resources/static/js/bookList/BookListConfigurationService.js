angular.module('bookListModule')
    .provider("bookListConfigurationService",function(){
        var searchBookServiceBaseUrl;
        var searchMetadaTagServiceBaseUrl;
        var bookRepositoryServiceBaseUrl;
        var userInterfaceEndPointBaseUrl;
        var userInterfaceBookReaderEndPointBaseUrl;

        return {
            setSearchBookServiceBaseUrl : function(setting){
            if(angular.isDefined(setting)){
                searchBookServiceBaseUrl = setting;
                return this;
            } else{
                return searchBookServiceBaseUrl;
            }
           },
            setSearchMetadaTagServiceBaseUrl:function(setting){
               if(angular.isDefined(setting)){
                   searchMetadaTagServiceBaseUrl = setting;
                   return this;
               } else{
                   return searchMetadaTagServiceBaseUrl;
               }
           },
            setBookRepositoryServiceBaseUrl : function(setting){
                if(angular.isDefined(setting)){
                    bookRepositoryServiceBaseUrl = setting;
                    return this;
                } else{
                    return bookRepositoryServiceBaseUrl;
                }
            },
            setUserInterfaceBookReaderEndPointBaseUrl:function(setting) {
                if (angular.isDefined(setting)) {
                    userInterfaceBookReaderEndPointBaseUrl = setting;
                    return this;
                } else {
                    return userInterfaceBookReaderEndPointBaseUrl;
                }
            },
            setUserInterfaceEndPointBaseUrl : function(setting){
                if(angular.isDefined(setting)){
                    userInterfaceEndPointBaseUrl = setting;
                    return this;
                } else{
                    return userInterfaceEndPointBaseUrl;
                }
            },
           $get:function(){
               return {
                   searchBookServiceBaseUrl:searchBookServiceBaseUrl,
                   searchMetadaTagServiceBaseUrl:searchMetadaTagServiceBaseUrl,
                   bookRepositoryServiceBaseUrl:bookRepositoryServiceBaseUrl,
                   userInterfaceEndPointBaseUrl:userInterfaceEndPointBaseUrl,
                   userInterfaceBookReaderEndPointBaseUrl:userInterfaceBookReaderEndPointBaseUrl
               }
           }
       }
    });
