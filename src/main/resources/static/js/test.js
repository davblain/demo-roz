    var stompClient=null;
    function sign_in() {
        $.ajax({
            type:'POST',
            url: "api/sign_in",
            contentType: "application/json",
            data: JSON.stringify({"username":"admin","password":"587238"}),
            dataType: "text"
            }).then(data => connect(data));
    }
    function connect(token) {
        if(stompClient===null) {
            this.socket = new SockJS('/ws');
            this.stompClient = Stomp.over(this.socket);
            this.stompClient.connect({"Authorization":token},function (frame) {
                stompClient.subscribe("/user/queue/private", function (change) {
                    console.log(change);
                });
            });
        }
    }
