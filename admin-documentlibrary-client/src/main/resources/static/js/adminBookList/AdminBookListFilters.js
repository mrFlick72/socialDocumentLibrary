angular.module("adminBookListModule")
    .filter("isPublishedFilter",function(){
        return function(value,filtringField,doFiltring){
            var result = [];
            if(doFiltring){
                if(angular.isArray(value)){
                    value.forEach(function(item){
                        if(item[filtringField]===false){
                            result.push(item);
                        }
                    });
                }
            } else{
                result = value;
            }
            return result;
        }
    })
    .filter("bookNameFilter",function(){
        return function(value,bookNameCriteria){
            var result = [];
            if(angular.isDefined(bookNameCriteria) && !(bookNameCriteria == "")){
                bookNameCriteria = bookNameCriteria.toUpperCase();
                if(angular.isArray(value)){
                    value.forEach(function(item){
                        if(item.name.toUpperCase().indexOf(bookNameCriteria) != -1){
                            result.push(item);
                        }
                    });
                }
            } else{
                result = value;
            }
            return result;
        }
    });
