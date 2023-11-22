// 환경에 따라 값을 조금 바꿔야 한다.
let chatInsertUrl = "/team15/chat/insertChat";
let chatListUrl = "/team15/user/mypage";
let chatUrl = "ws://10.41.1.127:8082/team15/ws/chat"; // host는 직접 지정해주자

let socket = new WebSocket(chatUrl);
let roomId = document.getElementById('roomId').value;
let id = document.getElementById('id').value;

// 소켓 시작
socket.onopen = function (e) {
    console.log('open server!')
    enterRoom(socket);
};

// 소켓 종료
socket.onclose = function (e) {
    console.log('disconnet');
    console.log('WebSocket closed:', e);
    location.href = chatListUrl;
};

// 에러발생
socket.onerror = function (e) {
    console.log(e);
};

// 메세지 보냄
socket.onmessage = function (e) {
    console.log(e.data);
    // JSON 문자열을 JavaScript 객체로 파싱
    let messageData = JSON.parse(e.data);
    let id = $("#id").val();

    // sender와 message를 추출
    let sender = messageData.sender;
    let message = messageData.message;

    let msgArea = document.querySelector('.msgArea');
    let newMsg = document.createElement('div');

    newMsg.innerText = `${message}`;
    // id와 sender 값 비교하여 클래스 추가
    console.log(id);
    console.log(sender);
    if (id === sender) {
        newMsg.className = 'my-message'; // 내 메시지일 경우
    } else {
        newMsg.className = 'others-message'; // 다른 사용자의 메시지일 경우
    }
    msgArea.append(newMsg);
    scrollDown();
};

// 채팅방에 들어오는 경우
function enterRoom(socket) {
    // Retrieve the CSRF token from the hidden input field
    var enterMsg = {
        "type": "ENTER",
        "roomId": roomId,
        "sender": id,
        "message": ""
    };
    socket.send(JSON.stringify(enterMsg));
}

// 채팅방 메세지 보내는 경우
function sendMsg() {
    // csrf 토큰 정보 가져오기
    const token = $("meta[name='_csrf']").attr("content")
    const header = $("meta[name='_csrf_header']").attr("content");
    let content = $("#content").val();

    const pattern = /<|>/i; // 스크립트 태그 방지를 위한 패턴 제외

    if(pattern.test(content)) {
        alert("메세지는 특수문자(<, >)를 제외한 글자 만 입력이 가능합니다")
        $("#content").val("");
        $("#content").focus();
        return false;
    } else {
        if(bad_words_ck(content) > 0) {
            alert('비속어가 감지되었습니다.')
            $("#content").val("");
            $("#content").focus();
            return false;
        }
    }

    let talkMsg = {
        "type": "TALK",
        "roomId": roomId,
        "sender": id,
        "message": content
    };
    socket.send(JSON.stringify(talkMsg));
    // ajax 요청
    $.ajax({
        type: "POST",
        url: chatInsertUrl,
        data: talkMsg,
        cache: false,
        success: function(result) {
        },
        error: function (err) {
            console.log(err)
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
    });

    $("#content").val("");
}

// 채팅방 나가는 경우
function quit() {
    let ck = confirm('채팅방을 나가시겠습니까?');

    if(!ck) {
        return false;
    }

    var quitMsg = {
        "type": "QUIT",
        "roomId": roomId,
        "sender": id,
        "message": ""
    };
    socket.send(JSON.stringify(quitMsg));
    location.href = chatListUrl;
    socket.close();
}

// 채팅 입력시 채팅창 맨 아래로 이동
function scrollDown(){
    let mySpace = document.getElementById("chat");
    mySpace.scrollTop = mySpace.scrollHeight;
}

$(document).ready(function () {
    // 채팅 보내기
    $("#sendBtn").click(()=> {
        $("#content").val("");
    });

    window.history.forward();
});

// 메시지 초기화
function rst() {
    $("#content").val("");
}

// 키보드 이벤트
function enterkey() {
    if (window.event.keyCode == 13) {
        // 엔터키가 눌렸을 때
        sendMsg();
    }
}