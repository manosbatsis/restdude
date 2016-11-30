define(['controllers/header', 'text!../../templates/header.html'], function(controller, template) {
	'use strict';

	function headerDirective() {
		return {
			restrict: 'A',
			controller: controller,
			template: template
		};
	}
	return headerDirective;
});