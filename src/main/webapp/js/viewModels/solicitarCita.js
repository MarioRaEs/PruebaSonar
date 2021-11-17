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
			self.botonCita = ko.observable(1);
			self.botonFecha = ko.observable(2);
			self.usuarios = ko.observableArray([]);
			self.nombre = ko.observable("");
			self.apellidos = ko.observable("");
			self.dni = ko.observable("");
			self.tipoUsuario = ko.observable("");
			self.centroAsignado = ko.observable("");
			self.dosisAdministradas = ko.observable("");
			self.localidad = ko.observable("");
			self.provincia = ko.observable("");		


			self.dniPaciente = ko.observable("");
			self.mensaje= ko.observable(2);
			self.mostrarSolicitarCita = ko.observable(1);

			self.message = ko.observable(null);
			self.error = ko.observable(null);

			self.cita = ko.observable("");
			self.fechaPrimeraDosis = ko.observable("");
			self.fechaSegundaDosis = ko.observable("");
			self.centrosSanitarios = ko.observableArray([]);

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
					url: "paciente/getCentros",
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
