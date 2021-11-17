define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
		'jquery' ], function(ko, app, moduleUtils, accUtils, $) {


	class GestionUserViewModel {
		constructor() {
			var self = this;
			
			self.usuarios = ko.observableArray([]);
			self.nombre = ko.observable("");
			self.email = ko.observable("");
			self.dni = ko.observable("");
			self.password = ko.observable("");
			self.centroAsignado = ko.observable("");
			self.tipoUsuario = ko.observable("");
			

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
		
	
		
		
		getUsuarios() {
			let self = this;
			let data = {
				url : "Usuario/getTodos",
				type : "get",
				contentType : 'application/json',
				success : function(response) {
				console.log(response);
					self.usuarios([]);
					 for (let i=0; i<response.length; i++) {
						let usuario = {
							nombre : response[i].nombre,
							email : response[i].email,
							password : response[i].password,
							dni : response[i].dni,						
							tipoUsuario : response[i].tipoUsuario,
							eliminar : function() {
								self.eliminarUsuario(response[i].dni); 
							},
							modificarUsuarios : function() {
								app.paciente = this;
								app.router.go({ path: "modificarUsuario" });
							},						
						};
						self.usuarios.push(usuario);
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
			let info = {
				nombre : this.nombre(),
				email : this.email(),
				dni : this.dni(),
				password : this.password(),
				tipoUsuario: this.tipoUsuario(),
			};
			let data = {
				data : JSON.stringify(info),
				url : "Usuario/add",
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
		
		modificarUsuario(){
			app.router.go({ path: "modificarUsuario" });
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

	return GestionUserViewModel;
});
