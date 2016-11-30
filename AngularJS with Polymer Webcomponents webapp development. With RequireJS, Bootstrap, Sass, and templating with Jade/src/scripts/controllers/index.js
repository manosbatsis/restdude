define([], function() {
	'use strict';

	function indexController($scope, $http) {
		$scope.data = {};
		$scope.data.libs = [];
		$http.get('data/data.json').then(function(result) {
			$scope.data.libs = result.data;
		});
	}
	indexController.$inject = ['$scope', '$http'];
	return indexController;
});