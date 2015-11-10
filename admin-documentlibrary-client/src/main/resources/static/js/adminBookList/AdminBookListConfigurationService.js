angular.module('adminBookListModule')
    .provider("adminBookListConfigurationService",function(){
        var searchBookServiceBaseUrl;
        var searchMetadaTagServiceBaseUrl;
        var userInterfaceEndPointBaseUrl;

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
                   userInterfaceEndPointBaseUrl:userInterfaceEndPointBaseUrl
               }
           }
       }
    });
