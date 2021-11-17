define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
		'jquery' ], function(ko, app, moduleUtils, accUtils, $) {


	class InicioViewModel {
		constructor() {
			var self = this;
			
			self.email = ko.observable("");
			self.password = ko.observable("");
			self.dni = ko.observable("");		
			
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
		
		login() {
			var self = this;
			var info = {
				email : this.email(),
				password : this.password()
			};
			var data = {
				data : JSON.stringify(info),
				url : "login/login",
				type : "post",
				contentType : 'application/json',
				success : function(response) {
					console.log(response.tipoUsuario);
					if(response.tipoUsuario=="Paciente"){
						app.navDataMenu.push({ path: 'home', detail : { label : 'Home'} });  
						app.router.go( { path : "homePaciente"} );	
					}else if(response.tipoUsuario=="Sanitario"){
						app.navDataMenu.push({ path: 'home', detail : { label : 'Home'} });  
						app.router.go( { path : "homeSanitario"} );	
					}else{
						app.navDataMenu.push({ path: 'home', detail : { label : 'Home'} });  
						app.router.go( { path : "homeAdmin"} );	
					}	
				},
				error : function(response) {
					console.log(response.responseJSON.message);
					self.error(response.responseJSON.message);
				}
			};
			$.ajax(data);
		}
		
		recoverPwd() {
			var self = this;
			var data = {
				url : "login/recoverPwd?email=" + self.email(),
				type : "get",
				contentType : 'application/json',
				success : function(response) {
					self.message("Si estás dado de alta, te habrá llegado un correo electrónico");
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

	return InicioViewModel;
});
