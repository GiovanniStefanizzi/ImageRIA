(function(){
	let userAlbumsTab;
	let otherUserAlbumsTab;
	let imagesTab;
	let imageDetailsTab;
	let commentsTab;
	
	let currentSet = 1;
	let images;
	
	
	window.addEventListener("load", () => {
		
        let pageOrchestrator = new PageOrchestrator();
        pageOrchestrator.start(); // inizializza i componenti
        pageOrchestrator.refresh(); // mostra i componenti
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
			
			otherUserAlbumsTab = new OtherUserAlbumsTab(
				document.getElementById("otheralbums-msg"),
				document.getElementById("otheralbums"),
				document.getElementById("otheralbums-body")
			);
			
			imagesTab = new ImagesTab(
                document.getElementById("image-msg"),
                document.getElementById("images"),
                document.getElementById("images-body")
            );
            
            imageDetailsTab = new ImageDetailsTab(
                document.getElementById("details-msg"),
                document.getElementById("details"),
                document.getElementById("details-body")
            );
                
                
            commentsTab = new CommentsTab(
				document.getElementById("comments-msg"),
				document.getElementById("comments"),
				document.getElementById("comments-body")
			);
			
		};
		
		this.refresh = function () {
			userAlbumsTab.reset();
			otherUserAlbumsTab.reset();
			imagesTab.reset();
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
			userAlbumsController.getAlbums();
		};
		
		this.show = function (albums){
			let self = this;
			self.update(albums);
		};	
		
		this.update = function(userAlbums){
			
			let len = userAlbums.length;
			let row, titleCell, dateCell, linkCell, anchor;
			
			if(len === 0){
				message.textContent = "There are no albums yet";
			}
			else{
				this.body.innerHTML = ""; //clear tbody
				this.message.textContent = "";
				
				let self = this;
				
				userAlbums.forEach(function (album){
					row = document.createElement("tr");
					
					
					//first col
					titleCell=document.createElement("td");
					titleCell.className = "album-cell";
					titleCell.textContent = album.title;
                    row.appendChild(titleCell);
                    
                    
                    //second col
                    dateCell = document.createElement("td");
                    dateCell.className = "album-cell";
                    dateCell.textContent = album.date;
                    row.appendChild(dateCell);
                    
                    
                    linkCell = document.createElement("td");
                    linkCell.className = "album-cell";
                    anchor = document.createElement("a");
                    let linkText = document.createTextNode("Show");
                    anchor.appendChild(linkText);
                    anchor.setAttribute('albumId', album.id);

					anchor.addEventListener("click", (e) => {

                        e.preventDefault();
                        currentSet = 1;
                        imagesTab.init(e.target.getAttribute("albumId"));  // quando faccio click su un album

                    }, false);
                    anchor.href = "#";
					
					linkCell.appendChild(anchor);

                    row.appendChild(linkCell);

                    self.body.appendChild(row);
				});
				 this.table.style.visibility = "visible";
			}
		}
	}
	
	
	function OtherUserAlbumsTab(message,table, body){
		this.message = message;
		this.table = table;
		this.body = body;
		
		
		this.reset = function (){
			this.body.innerHTML = "";
			this.table.style.visibility = "hidden";
			let otherUserAlbumsController = new OtherUserAlbumsController(this.message);
			otherUserAlbumsController.getAlbums();
		};
		
		this.show = function (albums){
			let self = this;
			self.update(albums);
		};	
		
		this.update = function(otherUserAlbums){
			
			let len = otherUserAlbums.length;
			let row, titleCell, dateCell, userCell, linkCell, anchor;
			
			if(len === 0){
				message.textContent = "There are no albums yet";
			}
			else{
				this.body.innerHTML = ""; //clear tbody
				this.message.textContent = "";
				
				let self = this;
				
				otherUserAlbums.forEach(function (album){
					row = document.createElement("tr");
					
					
					//first col
					titleCell=document.createElement("td");
					titleCell.textContent = album.title;
					titleCell.className = "album-cell";
                    row.appendChild(titleCell);
                    
                    //second col
                    userCell=document.createElement("td");
                    userCell.className = "album-cell";
					userCell.textContent = album.ownerUserName;
                    row.appendChild(userCell);
                    
                    //third col
                    dateCell = document.createElement("td");
                    dateCell.className = "album-cell";
                    dateCell.textContent = album.date;
                    row.appendChild(dateCell);
                    
                    
                    linkCell = document.createElement("td");
                    linkCell.className = "album-cell";
                    anchor = document.createElement("a");
                    let linkText = document.createTextNode("Show");
                    anchor.appendChild(linkText);
                    anchor.setAttribute('albumId', album.id);

					anchor.addEventListener("click", (e) => {

                        e.preventDefault();
                        currentSet = 1;
                        imagesTab.init(e.target.getAttribute("albumId"));  // quando faccio click su un album

                    }, false);
                    anchor.href = "#";
					
					linkCell.appendChild(anchor);

                    row.appendChild(linkCell);

                    self.body.appendChild(row);
				});
				 this.table.style.visibility = "visible";
			}
		}
	}
	
	
	
	function ImagesTab(message, table, body){
		
		this.message = message;
		this.table = table;
		this.body = body;
		
		this.reset = function () {
            this.body.innerHTML = "";
            this.table.style.visibility = "hidden";
        }
        
        this.init = function (albumId) {
            let photoController = new ImagesController(this.message);
            photoController.getPhotos(albumId);
        }
        
        this.show = function (images) {
            let self = this;
            self.update(images);
        };
        
        
        // compila la tabella con le foto che il server gli fornisce
        this.update = function (images) {
        	let len = images.length;
        	let row;
        	
        	 this.body.innerHTML = ""; // svuota il body della tabella
             //this.message.textContent = "";
             
             let self = this;
             row = document.createElement("tr");
        
        	if(currentSet>1){
				let scrollBack = document.createElement("button");
				scrollBack.textContent = "<";
				scrollBack.addEventListener("click", (e)=>{
					e.preventDefault();
                    currentSet = currentSet-1;
                    self.show(images);
				}, false);
				
				let btnCell = document.createElement("td");
				btnCell.appendChild(scrollBack);
				row.appendChild(btnCell);
			}
			
			let iter = (currentSet-1)*5;
			
			for(let i = 0; i<5 && iter<len; i++){
				
				let image = images[iter];
				let imageCell =  document.createElement("td");
				let title = document.createElement("p");
				title.textContent = image.title;
				
				let anchor = document.createElement("a");
			    let img = document.createElement("img");
			    img.className = "image-thumbnail";
			    img.src = "/ImageRIA" + image.source;
			    anchor.appendChild(img);
			    
			    anchor.setAttribute('idImage', image.id);

				
                anchor.addEventListener("mouseover", (e) => {

                    e.preventDefault();
                    let modal = document.getElementById("details");
                    modal.style.display = "block";           
                    let selected = e.target.closest("a").getAttribute("idImage");
                    imageDetailsTab.show(selected);
                     }, false); 
                     
                imageCell.appendChild(title);
                imageCell.appendChild(anchor);
                row.appendChild(imageCell);
                
                iter +=1;     
			}
			
			if(currentSet*5 < images.length){
				let scrollForward = document.createElement("button");
				scrollForward.className = "btn btn-primary";
				scrollForward.textContent = ">";
				scrollForward.addEventListener("click", (e)=>{
					e.preventDefault();
                    currentSet = currentSet+1;
                    self.show(images);
				}, false);
				
				let btnCell = document.createElement("td");
				btnCell.appendChild(scrollForward);
				row.appendChild(btnCell);
			}
			
			self.body.appendChild(row);
            this.table.style.visibility = "visible";
	    }
	}	
	
	
	function ImageDetailsTab(message, table, body) {
        this.message = message;
        this.table = table;            // intera tabella html dei meetings
        this.body = body;   // solo il body della tabella html dei meetings

        this.reset = function () {
            this.table.style.visibility = "hidden";
        }


        // chiama la update() se meeting non è vuota, altrimenti stampa l'alert
        this.show = function (selected) {
            let self = this;
            

            selectedImg = self.imageSearch(selected);

            if (selectedImg !== undefined){

                self.update(selectedImg);

               	commentsTab.reset();  // devo farlo ogni volta per aggiornare la tabella
                commentsTab.show(photoSelected.comments);

            }
            else this.message.textContent = "Problem on showing details";
        };


        this.imageSearch = function (imageId) {
            let p;

            images.forEach(function (image) {
                if(image.id == imageId){
                    p = image;
                }
            });

            return p;
        }


        this.update = function (image) {

            this.message.textContent = "";

            let detailsTitle = document.getElementById("details-title");
            let detailsDate = document.getElementById("details-date");
            let detailsDescription = document.getElementById("details-description");
            let detailsImage = document.getElementById("details-image");

			
            detailsTitle.textContent = image.title;
            detailsDate.textContent = image.date;
            detailsDescription.textContent = image.description;
            detailsImage.src = "/ImageRIA"+image.source;
            
 

            this.table.style.visibility = "visible";
        }

    }
	
	
	function CommentsTab(message, table, body) {
        this.message = message;
        this.table = table;            // intera tabella html dei meetings
        this.body = body;    // solo il body della tabella html dei meetings

        this.reset = function () {

            this.body.innerHTML = "";
            this.table.style.visibility = "hidden";
        }

        // chiama la update() se meeting non è vuota, altrimenti stampa l'alert
        this.show = function (comments) {

            let self = this;

            // se albums non è vuota...
            if (comments.length !== 0){

                self.update(comments);
                //commentForm.show();

            }
            else this.message.textContent = "No comments yet!";
            commentForm.show();
        };

        // compila la tabella con i meetings che il server gli fornisce
        this.update = function (comments) {

            let len = comments.length;
            let row, commentCell;

            if (len === 0) {  // controllo inutile ma per sicurezza XD
                message.textContent = "No comments yet!";

            } else {
                this.body.innerHTML = ""; // svuota il body della tabella
                this.message.textContent = "";

                let self = this;

                comments.forEach(function (comment) {

                    row = document.createElement("tr");

                    // prima cella della riga (titolo)
                    commentCell = document.createElement("td");
                    commentCell.textContent = comment.username + ": " + comment.text;
                    row.appendChild(commentCell);

                    self.body.appendChild(row);
                });
                this.table.style.visibility = "visible";
            }
        }

    }
	
	
	
	function UserAlbumsController(_message){
		this.message = _message;
		
		this.getAlbums = function(){
			let self = this;
			
			makeCall("GET", 'GetUserAlbums', null,
				function(req) {
					if(req.readyState === XMLHttpRequest.DONE){
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
	
	function OtherUserAlbumsController(_message){
	this.message = _message;
	
	this.getAlbums = function(){
		let self = this;
		
		makeCall("GET", 'GetOtherAlbums', null,
			function(req) {
				if(req.readyState === XMLHttpRequest.DONE){
					let resMessage = req.responseText;
					
					if(req.status === 200){
						let albums = JSON.parse(resMessage);
						otherUserAlbumsTab.show(albums);		
					}
					else{
						self.message.textContext = resMessage;
					}
				}
			});
		}
	}
	
	
	function ImagesController(_message){
		this.message = _message;
		this.getPhotos = function(albumId) {
	    	let self = this;
	    	makeCall("GET", "GetImages?album=" + albumId, null,
				function (req) {
                    if (req.readyState === XMLHttpRequest.DONE) {
                        let resMessage = req.responseText;

                        if (req.status === 200) {
                            images = JSON.parse(resMessage);
                            imagesTab.show(images);
                        } else {
							
                            self.message.textContent = _message;
                        }
                    }
                    
                }
			);
		}	
	
	
	}
	
})();