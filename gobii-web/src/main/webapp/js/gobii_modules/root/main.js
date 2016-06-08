System.register(["@angular/platform-browser-dynamic", '@ngrx/store', "./app.extractorroot", '../store/reducers'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var platform_browser_dynamic_1, store_1, app_extractorroot_1, reducers_1;
    return {
        setters:[
            function (platform_browser_dynamic_1_1) {
                platform_browser_dynamic_1 = platform_browser_dynamic_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (app_extractorroot_1_1) {
                app_extractorroot_1 = app_extractorroot_1_1;
            },
            function (reducers_1_1) {
                reducers_1 = reducers_1_1;
            }],
        execute: function() {
            platform_browser_dynamic_1.bootstrap(app_extractorroot_1.ExtractorRoot, store_1.provideStore(reducers_1.default));
        }
    }
});
//# sourceMappingURL=main.js.map