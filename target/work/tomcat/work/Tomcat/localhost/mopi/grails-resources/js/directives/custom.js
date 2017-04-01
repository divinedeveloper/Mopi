app.directive('clickLink', ['$location',
    function ($location) {
        return {
            link: function (scope, element, attrs) {
                element.on('click', function () {

                    scope.$apply(function () {

                        $location.path(attrs.clickLink);
                    });
                });
            }
        }
}]);

app.directive('uiJvectormap', [
function() {
  return {
    restrict: 'A',
    scope: {
      options: '='
    },
    link: function(scope, ele, attrs) {
      var options;
      options = scope.options;
      return ele.vectorMap(options);
    }
  };
}
])						

app.directive('phonenumber', function () {
  return function (scope, element, attrs) {
    element.bind("keydown", function (event) {

      if (!((event.keyCode == 46 ||
          event.keyCode == 8 ||
          event.keyCode == 37 ||
          event.keyCode == 39 ||
          event.keyCode == 9) ||
        $(this).val().length < 10 &&
        ((event.keyCode >= 48 && event.keyCode <= 57) ||
          (event.keyCode >= 96 && event.keyCode <= 105)))) {
        // Stop the event
        event.preventDefault();
        return false;
      }

    });
  }
})

app.directive('numbersOnly', function(){
   return {
     require: 'ngModel',
     link: function(scope, element, attrs, modelCtrl) {
       modelCtrl.$parsers.push(function (inputValue) {
           // this next if is necessary for when using ng-required on your input. 
           // In such cases, when a letter is typed first, this parser will be called
           // again, and the 2nd time, the value will be undefined
           if (inputValue == undefined) return '' 
           var transformedInput = inputValue.replace(/[^0-9]/g, ''); 
           if (transformedInput!=inputValue) {
              modelCtrl.$setViewValue(transformedInput);
              modelCtrl.$render();
           }         

           return transformedInput;         
       });
     }
   };
});
app.directive('integer', function () {
    return {
        require: 'ngModel',
        link: function (scope, ele, attr, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                return parseInt(viewValue);
            });
        }
    };
});

app.directive("passwordVerify", function() {
return {
    require: "ngModel",
    scope: {
        passwordVerify: '='
    },
    link: function(scope, element, attrs, ctrl) {
        scope.$watch(function() {
            var combined;
            
            if (scope.passwordVerify || ctrl.$viewValue) {
               combined = scope.passwordVerify + '_' + ctrl.$viewValue; 
            }                    
            return combined;
        }, function(value) {
            if (value) {
                ctrl.$parsers.unshift(function(viewValue) {
                    var origin = scope.passwordVerify;
                    if (origin !== viewValue) {
                        ctrl.$setValidity("passwordVerify", false);
                        return undefined;
                    } else {
                        ctrl.$setValidity("passwordVerify", true);
                        return viewValue;
                    }
                });
            }
        });
    }
};
});

app.directive('disabled', function ($compile) {
    return {
        restrict: 'A',
        priority: -99999,
        link: function (scope, element, attrs) {
            var oldNgFileSelect = attrs.ngFileSelect;
            console.log(oldNgFileSelect)
            if (oldNgFileSelect) {
                scope.$watch(attrs.disabled, function (val, oldval) {
                    console.log(val);
                    console.log(oldval);
                    console.log(!!val);
                    if (!!val) {
                        element.unbind('click');
                    } else if (oldval) {
                        attrs.$set('ngFileSelect', oldNgFileSelect);
                        element.bind('click', function () {
                            scope.$apply(attrs.ngFileSelect);
                        });
                    }
                });
            }
        }
    };
});
