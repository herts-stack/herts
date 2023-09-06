"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.TestModel = exports.User = exports.NestedCustomModel = exports.CustomModel = void 0;
var CustomModel = /** @class */ (function () {
    function CustomModel(id, data, nestedCustomModel) {
        this.id = id;
        this.data = data;
        this.nestedCustomModel = nestedCustomModel;
    }
    return CustomModel;
}());
exports.CustomModel = CustomModel;
var NestedCustomModel = /** @class */ (function () {
    function NestedCustomModel(nestedId, pointer) {
        this.nestedId = nestedId;
        this.pointer = pointer;
    }
    return NestedCustomModel;
}());
exports.NestedCustomModel = NestedCustomModel;
var User = /** @class */ (function () {
    function User(id, name, createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
    return User;
}());
exports.User = User;
var TestModel = /** @class */ (function () {
    function TestModel(id, name) {
        this.id = id;
        this.name = name;
    }
    return TestModel;
}());
exports.TestModel = TestModel;
