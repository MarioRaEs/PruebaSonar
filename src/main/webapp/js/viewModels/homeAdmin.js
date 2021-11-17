define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
		'jquery' ], function(ko, app, moduleUtils, accUtils, $) {


	class InicioViewModel {
		constructor() {
			var self = this;
			
			self.mostrarUsuario = ko.observable(0);
			self.mostrarCentro = ko.observable(0);
			
			self.centros = ko.observableArray([]);
			self.usuarios = ko.observableArray([]);
			self.nombre = ko.observable("");
			self.apellidos = ko.observable("");
			self.dni = ko.observable("");
			self.tipoUsuario = ko.observable("");
			self.centroAsignado = ko.observable("");
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
		
		mostrarUsuarios(){
			var self = this;
			self.mostrarUsuario(1);
			self.mostrarCentros(0);

		}

		mostrarCentros(){
			var self = this;
			self.mostrarUsuarios(0);
			self.mostrarCentro(1);
		}
		
		
		
		getUsuarios() {
			let self = this;
			let data = {
				url : "Usuario/getTodos",
				type : "get",
				contentType : 'application/json',
				success : function(response) {
					self.usuarios([]);
					 for (let i=0; i<response.length; i++) {
						let paciente = {
							nombre : response[i].nombre,
							apellidos: response[i].apellidos,
							dni : response[i].dni,
							tipoUsuario : response[i].tipoUsuario,
							centroAsignado : response[i].centroAsignado,
							dosisAdministradas : response[i].dosisAdministradas,
							localidad : response[i].localidad,
							provincia : response[i].provincia,
							eliminar : function() {
								self.eliminarUsuario(response[i].dni); 
							},
							modificarUsuarios : function() {
								app.paciente = this;
								self.modificarUsuarios();
								self.paciente = ko.observable();
								self.paciente = app.paciente;
								self.nombre2 = ko.observable(self.paciente.nombre);
							},						
						};
						self.usuarios.push(paciente);
					}
				},
				error : function(response) {
					self.error(response.responseJSON.errorMessage);
				}
			};
			$.ajax(data);
		}

		eliminarUsuario(dni) {
			let self = this;
			console.log("Hey");
			let data = {
				url : "paciente/eliminarUsuario/" + dni,
				type : "delete",
				contentType : 'application/json',
				success : function(response) {
					self.message("Usuario eliminado ");
					self.getUsuarios();
				},
				error : function(response) {
					self.error(response.responseJSON.errorMessage);
				}
			};
			$.ajax(data);
		}

		add() {
			var self = this;
			var key = document.getElementsByName("tipoUsuario");
				for (var i = 0; i < key.length; i++) {
					if (key[i].checked)
						var key2= key[i].value;
				}
			let info = {
				nombre : this.nombre(),
				apellidos: this.apellidos(),
				dni : this.dni(),
				tipoUsuario: key2,
				localidad: this.localidad(),
				provincia: this.provincia(),
				
			};
			let data = {
				data : JSON.stringify(info),
				url : "paciente/add",
				type : "put",
				contentType : 'application/json',
				success : function(response) {
					self.message("Usuario guardado");
					self.getUsuarios();
					self.error(null);
				},
				error : function(response) {
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

		connected() {
			accUtils.announce('Inicio page loaded.');
			document.title = "Inicio";
			this.getUsuarios();
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
