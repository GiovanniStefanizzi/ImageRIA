(function(){
	let userAlbumsTab;
	let otherUserAlbumsTab;
	let imagesTab;
	let imageDetailsTab;
	let commentsTab;
	let commentForm;
	
	let currentSet = 1;
	let images;
	let selectedImg;
	
	window.addEventListener("load", () => {
		
        let pageOrchestrator = new PageOrchestrator();
        pageOrchestrator.start(); // inizializza i componenti
        pageOrchestrator.refresh(); // mostra i componenti
        document.getElementById('close-btn').click();
        
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
				document.getElementById("comment-msg"),
				document.getElementById("comments"),
				document.getElementById("comments-body")
			);
			
			
			
			commentForm = new CommentForm(
                document.getElementById("comments-field"),
                document.getElementById("comments-form")
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
                        document.getElementById('close-btn').click();
						console.log("fra famme vere  " + document.getElementById('close-btn'))
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
                        document.getElementById('close-btn').click();
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
                    let modal = document.getElementById("modal");
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
                commentsTab.show(selectedImg.comments);

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
        };

        // chiama la update() se meeting non è vuota, altrimenti stampa l'alert
        this.show = function (comments) {
            let self = this;
            let commentList = comments
            let msg = document.getElementById("comment-msg");

            // se albums non è vuota...
            if (commentList.length !== 0){
				msg.textContent = "";
                self.update(commentList);
                //commentForm.show();

            } else { 
				let msg = document.getElementById("comment-msg");
				msg.textContent = "no commenti della fratella";
			}
			
            //else this.message.textContent = "No comments yet!";
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
                //message.textContent = "";
				
                let self = this;

                comments.forEach(function (comment) {

                    row = document.createElement("tr");

                    // prima cella della riga (titolo)
                    commentCell = document.createElement("td");
                    commentCell.textContent = comment.userName + ": " + comment.text;
                    row.appendChild(commentCell);

                    self.body.appendChild(row);
                });
                this.table.style.visibility = "visible";
            }
        }

    }
    
    
    function CommentForm(field, form) {
        this.field = field;
        this.form = form;

        this.reset = function () {
            this.field.style.visibility = "hidden";
        }

        this.show = function () {
            this.field.style.visibility = "visible";
        };

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
	
	
		
	
	
	
	// COMMENT FORM
    document.getElementById("comment-btn").addEventListener('click', (e) => {
        e.preventDefault();

        let form = e.target.closest("form");

        // imposto l'immagine selezionata nel campo selectedImg della form
        document.getElementById("comments-form").elements["selectedImg"].value = selectedImg.id;


        if (form.checkValidity()) {
            makeCall("POST", 'Comments', e.target.closest("form"),
                function(req) {
                    if (req.readyState === XMLHttpRequest.DONE) {

                        let message = req.responseText;     // risposta del server
                        switch (req.status) {
                            case 200:
                                form.reset();
                                let comments = JSON.parse(message);
                                selectedImg.comments = comments;
                                commentsTab.show(selectedImg.comments);

                                break;
                            case 400: // bad request
                                document.getElementById("comment-msg").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("comment-msg").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("comment-msg").textContent = message;
                                break;
                        }
                    }
                }, false);
        } else {
            form.reportValidity();
        }
    });
    
    
    
    // MODAL WINDOW
    document.getElementById("close-btn").addEventListener('click', () => {
        let modal = document.getElementById("modal");
        modal.style.display = "none";
    });

    // Quando clicki fuori dall finestra modale, questa si chiude
    window.onclick = function(event) {
        let modal = document.getElementById("modal");
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

	
	
	
	
	
	
	
	
})();