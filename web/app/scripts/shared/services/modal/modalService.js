/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.service("ModalService", function($modal) {
		var modalDefaults,
			modalOptions,
			show;

		modalDefaults = {
			templateUrl: "/scripts/shared/services/modal/modalView.html"
		};

		modalOptions = {
			confirmButtonText: "OK",
			cancelButtonText: "Cancel",
			confirmButtonType: "primary",
			cancelButtonType: "link",
			headerText: "Are you sure?"
		};

		show = function(customModalDefaults, customModalOptions) {
			var tempModalDefaults = {},
				tempModalOptions = {};

			angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);
			angular.extend(tempModalOptions, modalOptions, customModalOptions);

			if (!tempModalDefaults.controller) {
				tempModalDefaults.controller = function($scope, $modalInstance) {
					$scope.modalOptions = tempModalOptions;
					$scope.modalOptions.confirm = function(result) {
						$modalInstance.close(result);
					};
					$scope.modalOptions.cancel = function(result) {
						$modalInstance.dismiss("cancel");
					};
				};
			}

			return $modal.open(tempModalDefaults).result;
		};


		this.showModal = function(customModalDefaults, customModalOptions) {
			return show(customModalDefaults, customModalOptions);
		};

		this.showDeleteModal = function(customModalDefaults, customModalOptions) {
			if (!customModalOptions) {
				customModalOptions = {};
			}
			customModalOptions.confirmButtonText = "Delete";
			customModalOptions.confirmButtonType = "danger";
			customModalOptions.headerText = "Delete?";
			return show(customModalDefaults, customModalOptions);
		};
	});

}());
