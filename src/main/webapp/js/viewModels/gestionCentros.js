define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
		'jquery' ], function(ko, app, moduleUtils, accUtils, $) {


	class GestionCenterViewModel {
		constructor() {
			var self = this;
			
			self.centros = ko.observableArray([]);
			self.id = ko.observable("");
			self.nombre = ko.observable("");
			self.horaInicio = ko.observable("");
			self.horaFin = ko.observable("");
			self.dosisTotales = ko.observable("");
			self.aforo = ko.observable("");
			self.localidad = ko.observable("");
			self.provincia = ko.observable("");		
			

			self.mensaje= ko.observable(2);
			self.mostrarSolicitarCita = ko.observable(1);
			
			self.message = ko.observable();
			self.error = ko.observable();
			
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
		
		
		
		
		
		getCentros() {
			let self = this;
			let data = {
				url : "centro/getTodos",
				type : "get",
				contentType : 'application/json',
				success : function(response) {
					self.centros([]);
					 for (let i=0; i<response.length; i++) {
						let centro = {
							id : response[i].id,
							nombre : response[i].nombre,
							dosisTotales: response[i].dosisTotales,
							aforo : response[i].aforo,
							horaInicio : response[i].horaInicio,
							horaFin : response[i].horaFin,
							localidad : response[i].localidad,
							provincia : response[i].provincia,
							eliminar : function() {
								self.eliminarUsuario(response[i].dni); 
							},
							
							modificarCentros : function() {
								app.centro = this;
								app.router.go({ path: "modificarCentro" });
							},
												
						};
						
						self.centros.push(centro);
					}
					if(response.length>0){
						document.getElementById("horaInicio").style.display='none';
						document.getElementById("aforo").style.display='none';
						document.getElementById("horaFin").style.display='none';
					}else{
						document.getElementById("horaInicio").style.display='inline';
						document.getElementById("aforo").style.display='inline';
						document.getElementById("horaFin").style.display='inline';
					}
				},
				error : function(response) {
					self.error(response.responseJSON.errorMessage);
				}
			};
			$.ajax(data);
		}

		eliminarCentro(id) {
			let self = this;
			let data = {
				url : "centro/eliminarCentro/" + id,
				type : "delete",
				contentType : 'application/json',
				success : function(response) {
					console.log(response);
					self.message("Centro eliminado ");
					self.getCentros();
					self.error(null);
					console.log(self.error);
					self.getCentros();
				},
				error : function(response) {
					self.error(response.responseJSON.message);
				}
				
			};
			$.ajax(data);
		}
		
		
		add() {
			var self = this;
			let info = {
				nombre : this.nombre(),
				dosisTotales: this.dosisTotales(),
				aforo : this.aforo(),
				localidad: this.localidad(),
				provincia: this.provincia(),
				horaInicio : this.horaInicio(),
				horaFin : this.horaFin(),
			};
			let data = {
				data : JSON.stringify(info),
				url : "centro/add",
				type : "put",
				contentType : 'application/json',
				success : function(response) {
					self.message("Centro guardado");
					self.getCentros();
					self.error(null);
				},
				error : function(response) {
					self.error(response.responseJSON.message);
				}
			};
			$.ajax(data);
		}
		
		modificarCentro(id){
			app.idc = id;
			app.centro = this;
			app.router.go({ path: "modificarCentro" });
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

	return GestionCenterViewModel;
});
