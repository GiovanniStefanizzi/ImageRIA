(function(){
	let userAlbumsTab;
	let otherAlbumsTab;
	
	
	window.addEventListener("load", () => {
		
        let pageOrchestrator = new PageOrchestrator();
        pageOrchestrator.start(); // inizializza i componenti
        //pageOrchestrator.refresh(); // mostra i componenti
    }, false);
	
	
	
	function PageOrchestrator(){
		this.start = function () {
			let username = sessionStorage.getItem('username')
			let message = document.getElementById("greeting-msg")
			message.textContent = "welcome back, " + username + "!";	
			
			userAlbumsTab = new UserAlbumsTab(
				document.getElementById("useralbums-msg"),
				document.getElementById("useralbums"),
				document.getElementById("useralbums-body")
			);
			
		};
		
		this.refresh = function () {
			userAlbumsTab.reset();
			
		};
		
		
		
		
	}
	
	
	function UserAlbumsTab(message,table, body){
		this.message = message;
		this.table = table;
		this.body = body;
		
		
		this.reset = function (){
			this.body.innerHTML = "";
			this.table.style.visibility = "hidden";
			
			let userAlbumsController = new UserAlbumsController(this.message);
			userAlbumController.getAlbums();
		};
		
		this.show = function (albums){
			let self = this;
			self.update(albums);
		};	
		
	}
	
	function UserAlbumsController(_message){
		this.message = _message;
		
		this.getAlbums = function(){
			let self = this;
			
			makeCall("GET", 'GetUserAlbums', null,
				function(req) {
					if(req.readyStare === XMLHttpRequest.DONE){
						let resMessage = req.responseText;
						
						if(req.status === 200){
							let albums = JSON.parse(resMessage);
							userAlbumsTab.show(albums);
							
						}
						else{
							self.message.textContext = resMessage;
						}
					}
				}
			);
		}
	}
	
	






	
})();