System.register(["@angular/core", "../../model/name-id"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, name_id_1;
    var DtoRequestItemNameIds;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            }],
        execute: function() {
            DtoRequestItemNameIds = (function () {
                function DtoRequestItemNameIds() {
                }
                DtoRequestItemNameIds.prototype.getUrl = function () {
                    return "load/nameidlist";
                }; // getUrl()
                DtoRequestItemNameIds.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": "READ",
                        "dtoHeaderAuth": { "userName": null, "password": null, "token": null },
                        "dtoHeaderResponse": { "succeeded": true, "statusMessages": [] },
                        "entityType": "DBTABLE",
                        "entityName": "datasetnames",
                        "namesById": {},
                        "filter": null
                    });
                };
                DtoRequestItemNameIds.prototype.resultFromJson = function (json) {
                    var returnVal = [];
                    console.log(json);
                    var arrayOfIds = Object.keys(json.namesById);
                    arrayOfIds.forEach(function (id) {
                        var currentVal = json.namesById[id];
                        returnVal.push(new name_id_1.NameId(id, currentVal));
                    });
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemNameIds = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [])
                ], DtoRequestItemNameIds);
                return DtoRequestItemNameIds;
            }());
            exports_1("DtoRequestItemNameIds", DtoRequestItemNameIds); // DtoRequestItemNameIds() 
        }
    }
});
//# sourceMappingURL=dto-request-item-nameids.js.map