/**
 * 
 */
 (function() { // avoid variables ending up in the global scope

    document.getElementById("login-btn").addEventListener('click', (e) => {
        e.preventDefault();

        const form = e.target.closest("form");

        if (form.checkValidity()) {
            makeCall("POST", 'Login', e.target.closest("form"),
                function(req) {
                    if (req.readyState === XMLHttpRequest.DONE) {

                        var message = req.responseText;     // risposta del server
                        switch (req.status) {
                            case 200:
                                sessionStorage.setItem('username', message);
                                window.location.href = "home.html";
                                break;
                            case 400: // bad request
                                document.getElementById("error-msg").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("error-msg").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("error-msg").textContent = message;
                                break;
                        }
                    }
                }, false);
        } else {
            form.reportValidity();
        }
    });

})();