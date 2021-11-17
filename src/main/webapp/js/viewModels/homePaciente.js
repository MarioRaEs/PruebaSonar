define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
		'jquery' ], function(ko, app, moduleUtils, accUtils, $) {


	class InicioViewModel {
		constructor() {
			var self = this;
			
			self.botonCita = ko.observable(1);
			self.botonFecha = ko.observable(2);
			
			self.usuarios = ko.observableArray([]);
			self.nombre = ko.observable("");
			self.apellidos = ko.observable("");
			self.dniPaciente = ko.observable("");
			self.tipoUsuario = ko.observable("");
			self.centroAsignado = ko.observable("");
			self.dosisAdministradas = ko.observable("");	
			
			self.cita = ko.observable("");
			self.fechaPrimeraDosis = ko.observable("");
			self.fechaSegundaDosis = ko.observable("");
			self.centrosSanitarios = ko.observableArray([]);
			self.citas = ko.observableArray([]);

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
		
		//Gestionar este metodo para que se selecciona el centro de la bbdd
		solicitarCita() {
			let self = this;
			
			var time = new Date().getTime();
			var key3 = document.getElementById("menuCentros");
			
			let info = {
					centroAsignado: key3.options[key3.selectedIndex].value,
					dni : document.getElementById("dniUsuario").value,
			};
			
			let data = {
					
					data: JSON.stringify(info),
					url : "cita/add",
					type : "put",
					contentType : 'application/json',
					success : function(response) {

						var date = new Date (response.fechaPrimeraDosis)
						date.toLocaleString();
						var date2 = new Date (response.fechaSegundaDosis)
						date2.toLocaleString();

						self.fechaPrimeraDosis(date.toLocaleString());
						self.fechaSegundaDosis(date2.toLocaleString());
						self.centroAsignado(key3.options[key3.selectedIndex].value);
						self.message("Cita Creada");	
						self.botonCita(2);
						self.botonFecha(1);
					},
					error : function(response) {
						self.error(response.responseJSON.errorMessage);
					}
			};
			$.ajax(data);
		}
		
		getCentros() {
			let self = this;
			let data = {
					url: "Usuario/getCentros",
					type: "get",
					contentType: 'application/json',
					success: function(response) {
						self.centrosSanitarios(response);
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
			};
			$.ajax(data);
		}
		
		
		logout() {
			let self = this;
			let data = {
				url : "login/logout",
				type : "post",
				contentType : 'application/json',
				success : function(response) {
					app.router.go( { path : "login" } );
				},
				error : function(response) {
					self.error(response.responseJSON.errorMessage);
				}
			};
			$.ajax(data);
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
			this.getCentros();
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
