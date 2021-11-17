define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery' ], function(ko, app, moduleUtils, accUtils, $) {


	class ModCenterViewModel {
		constructor() {
			var self = this;

			self.centro = ko.observable();
			self.centro = app.centro;

			self.nombre = ko.observable(app.centro.nombre);
			self.dosisTotales = ko.observable(app.centro.dosisTotales);
			self.aforo = ko.observable(app.centro.aforo);
			self.horaInicio = ko.observable(app.centro.horaInicio);
			self.horaFin = ko.observable(app.centro.horaFin);
			self.localidad = ko.observable(app.centro.localidad);
			self.provincia = ko.observable(app.centro.provincia);		

			self.message = ko.observable(null);
			self.error = ko.observable(null);

			// Header Config
			self.headerConfig = ko.observable({
				'view' : [],
				'viewModel' : null
			});
			moduleUtils.createView({
				'viewPath' : 'views/header.html'
			}).then(function(view) {
				self.headerConfig({
					'view' : view,
					'viewModel' : app.getHeaderModel()
				})
			})
		}	

		modificarCentro() {
			console.log("Llego con centro " + app.centro);
			let self = this;

			let info = {
					id : app.centro.id,
					nombre : this.nombre(),
					dosisTotales: this.dosisTotales(),
					aforo : this.aforo(),
					horaInicio : this.horaInicio(),
					horaFin : this.horaFin(),
					localidad: this.localidad(),
					provincia: this.provincia(),
			};

			let data = {
					data : JSON.stringify(info),
					url : "centro/modificarCentro/" + app.centro.id,
					type : "put",
					contentType : 'application/json',
					success : function(response) {
						app.router.go({ path: "gestionCentros" });
						self.message("Centro modificado");
					},
					error : function(response) {
						self.error(response.responseJSON.errorMessage);
					}
			};
			$.ajax(data);
		}


		connected() {
			accUtils.announce('Inicio page loaded.');
			document.title = "Inicio";
		};

		disconnected() {
			// Implement if needed
		};

		transitionCompleted() {
			// Implement if needed
		};
	}

	return ModCenterViewModel;
});
