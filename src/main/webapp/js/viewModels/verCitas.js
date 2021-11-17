define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery' ], function(ko, app, moduleUtils, accUtils, $) {


	class InicioViewModel {
		constructor() {
			var self = this;

			self.mostrarsolicitarCita = ko.observable(1);
			self.mostrarverCitas = ko.observable(1);
			self.mostrarMensaje = ko.observable(2);
			self.mostrarGestionUsuario = ko.observable(1);
			self.mostrarModificacionUsuario= ko.observable(1);

			self.citas = ko.observableArray([]);
			self.nombre = ko.observable("");
			self.apellidos = ko.observable("");
			self.dniPaciente = ko.observable("");
			self.tipoUsuario = ko.observable("");
			self.centroAsignado = ko.observable("");
			self.fechaPrimeraDosis = ko.observable("");
			self.fechaSegundaDosis = ko.observable("");
			self.dosisAdministradas = ko.observable("");
			self.localidad = ko.observable("");
			self.provincia = ko.observable("");		

			self.mensaje= ko.observable(2);
			self.mostrarSolicitarCita = ko.observable(1);

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

		solicitarCita(){
			var self = this;
			self.mostrarsolicitarCita(2);
			self.mensaje(1);
			self.mostrarverCitas(1);
			self.mostrarGestionUsuario(1);
			self.mostrarModificacionUsuario(1);
		}

		verCitas(){
			var self = this;
			self.mostrarsolicitarCita(1);
			self.mensaje(1);
			self.mostrarverCitas(2);
			self.mostrarGestionUsuario(1);
			self.mostrarModificacionUsuario(1);
		}

		gestionUsuarios(){
			var self = this;
			self.mostrarsolicitarCita(1);
			self.mensaje(1);
			self.mostrarverCitas(1);
			self.mostrarGestionUsuario(2);
			self.mostrarModificacionUsuario(1);
		}

		modificarUsuarios(){
			var self = this;
			app.paciente = this;
			self.mostrarsolicitarCita(1);
			self.mensaje(1);
			self.mostrarverCitas(1);
			self.mostrarGestionUsuario(1);
			self.mostrarModificacionUsuario(2);
		}

		getCitas() {
			let self = this;
			let data = {
					url : "cita/getTodos",
					type : "get",
					contentType : 'application/json',
					success : function(response) {
						self.citas([]);
						var centroAsignado;
						var date;
						var date2;
						for (let i=0; i<response.length; i++) {
							date = new Date(response[i].fechaPrimeraDosis);
							date2 = new Date(response[i].fechaSegundaDosis);
							centroAsignado = response[i].nombreCentro;
							let cita = {
									id : response[i].id,
									dniPaciente : response[i].dniPaciente,
									centroAsignado: response[i].nombreCentro,
									fechaPrimeraDosis: date.toLocaleString(),
									fechaSegundaDosis : date2.toLocaleString(),
									eliminar : function() {
										self.eliminarCita(response[i].id);
									},					
							};
							self.citas.push(cita);
						}
					},
					error : function(response) {
						self.error(response.responseJSON.errorMessage);
					}
			};
			$.ajax(data);
		}

		eliminarCita(id) {
			let self = this;
			let data = {
					url : "cita/eliminarCita/" + id,
					type : "delete",
					contentType : 'application/json',
					success : function(response) {
						self.message("Cita eliminada ");
						self.getCitas();
					},
					error : function(response) {
						self.error(response.responseJSON.errorMessage);
					}
			};
			$.ajax(data);
		}
		
		modificarCita(id) {
			let self = this;
			
			let data = {
					url : "cita/modificarCita/" + id,
					type : "put",
					contentType : 'application/json',
					success : function(response) {
						self.fechaPrimeraDosis(response.fechaPrimeraDosis);
						self.fechaSegundaDosis(response.fechaSegundaDosis);
						self.centroAsignado(response.nombreCentro);
						self.message("Cita Creada");
						self.getCitas();
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
			this.getCitas();
		};

		disconnected() {
			// Implement if needed
		};

		transitionCompleted() {
			// Implement if needed
		};
	}

	return InicioViewModel;
});
