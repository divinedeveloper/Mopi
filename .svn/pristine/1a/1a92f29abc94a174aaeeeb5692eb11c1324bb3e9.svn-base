<%----%>

<!doctype html>
<!--[if lt IE 8]>         <html class="no-js lt-ie8"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1'>
        <title>Web Application</title>
        <meta name="description" content="Responsive Admin Web App">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

        <link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,400,600,300,700' rel='stylesheet' type='text/css'>
      <r:require modules="core"/>
      <r:layoutResources/>

</head>
    <body>
    <body data-ng-app="app"
          id="app"
          class="app"
          data-custom-page 
          data-off-canvas-nav
          data-ng-controller="HomeCtrl"
          data-ng-class=" {'layout-boxed': admin.layout === 'boxed'} "
          >
           
 <toaster-container toaster-options="{'time-out': 3000, 'close-button':true}"></toaster-container>

        <section data-ng-include=" 'static/partials/header.html' "
                 id="header"
                 class="header-container "
                 data-ng-class="{ 'header-fixed': admin.fixedHeader,
                                  'bg-white': ['11','12','13','14','15','16','21'].indexOf(admin.skin) >= 0,
                                  'bg-dark': admin.skin === '31',
                                  'bg-primary': ['22','32'].indexOf(admin.skin) >= 0,
                                  'bg-success': ['23','33'].indexOf(admin.skin) >= 0,
                                  'bg-info-alt': ['24','34'].indexOf(admin.skin) >= 0,
                                  'bg-warning': ['25','35'].indexOf(admin.skin) >= 0,
                                  'bg-danger': ['26','36'].indexOf(admin.skin) >= 0
                 }"></section>

        <div class="main-container" ng-init="admin.menu='horizontal'">
            <!--aside data-ng-include=" 'static/partials/nav.html' "
                   id="nav-container"
                   class="nav-container"	
                   data-ng-class="{ 'nav-fixed': admin.fixedSidebar,
                                    'nav-horizontal': admin.menu === 'horizontal',
                                    'nav-vertical': admin.menu === 'vertical',
                                    'bg-white': ['31','32','33','34','35','36'].indexOf(admin.skin) >= 0,
                                    'bg-dark': ['31','32','33','34','35','36'].indexOf(admin.skin) < 0
                   }">
            </aside-->

            <div id="content" class="content-container">
             <ol class="breadcrumb" ng-if="breadCrumb.hideBreadCrumb == false" ng-cloak class="ng-cloak">
                        <li ng-repeat="data in breadcrum"><a href="#/{{data.url}}">{{data.name}}</a></li>
                   
                    </ol>
                <section data-ng-view
                         class="view-container at-view-slide-in-right"></section>
            </div>
        </div>
<script src="https://maps.google.com/maps/api/js?libraries=places&sensor=false&key=AIzaSyBScO9luOtjbx6vyU4qwVo6QQ-YvEvugMM"></script>
 <r:layoutResources/>
 
        
<%--          <asset:javascript src="application.js"/>--%>
<%--    <asset:deferredScripts />--%>
 
    </body>
</html>
