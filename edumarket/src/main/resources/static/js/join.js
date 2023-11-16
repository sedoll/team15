$(document).ready(function(){
    $("#idCheckBtn").click(function(){

        if($("#id").val()==""){
            alert("아이디를 입력하지 않으셨습니다.");
            $("#id").focus();
            return false;
        }

        // 정규표현식 패턴
        let pattern = /^(?=.*[a-z].*[a-z])(?=.*[0-9].*[0-9])[a-z0-9]*$/;
        // 유효성 검사
        if (!pattern.test($("#id").val())) {
            alert("아이디는 영어와 숫자가 최소 2자리 이상씩 포함되어야 합니다.");
            $("#id").focus();
            return false;
        }

        const token = $("meta[name='_csrf']").attr("content")
        const header = $("meta[name='_csrf_header']").attr("content");
        var test = {name : $("#id").val()}//전송되어질 데이터를 객체로 묶음
        $.ajax({
            url:"/common/idCheck",	//아이디가 전송되어질 곳
            type:"post",		//전송방식
            data:JSON.stringify(test),
            dataType:"json",
            contentType: "application/json",
            cache: false,
            success:function(result){
                console.log(result);
                var idChk = result;	//true 또는 false를 받음
                if(idChk==false){	//사용할 수 없는 아이디
                    $("#idCheck").val("false");
                    $("#msg").html("<strong style='color:red'>기존에 사용되고 있는 아이디 입니다. 다시 입력하시기 바랍니다.</strong>");
                    $("#id").focus();
                } else if(idChk==true){	//사용 가능한 아이디
                    $("#idCheck").val("true");
                    $("#msg").html("<strong style='color:blue'>사용가능한 아이디 입니다.</strong>");
                } else if(idChk==""){
                    $("#msg").html("<strong>아이디가 확인되지 않았습니다. 다시 시도해주시기 바랍니다.</strong>");
                }
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
        });
    });
    $("#emailCheckBtn").click(function(){
        if($("#email").val()==""){
            alert("이메일을 입력하지 않으셨습니다.");
            $("#email").focus();
            return false;
        }

        // 정규표현식 패턴
        let pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*[a-zA-Z]$/;
        // 유효성 검사
        if (!pattern.test($("#email").val())) {
            alert("이메일 형식이 옳바르지 않습니다.");
            $("#email").focus();
            return false;
        }

        // Retrieve the CSRF token from the hidden input field
        const token = $("meta[name='_csrf']").attr("content")
        const header = $("meta[name='_csrf_header']").attr("content");
        var params = {email: $("#email").val()} //전송되어질 데이터를 객체로 묶음
        $.ajax({
            url:"/common/emailCheck",	//아이디가 전송되어질 곳
            type:"post",		//전송방식
            dataType:"json",	//데이터 반환 방식
            data:params,		//전송방식이 post인 경우 객체로 묶어서 전송
            cache: false,
            success:function(result){
                console.log(result);
                var emailChk = result;	//true 또는 false를 받음
                if(emailChk==false){	//사용할 수 없는 이메일
                    $("#emailCheck").val("false");
                    $("#msg2").html("<strong style='color:red'>기존에 사용되고 있는 이메일 입니다. 다시 입력하시기 바랍니다.</strong>");
                    $("#email").focus();
                } else if(emailChk==true){	//사용 가능한 이메일
                    $("#emailCheck").val("true");
                    $("#msg2").html("<strong style='color:blue'>사용가능한 이메일입니다.</strong>");
                } else if(emailChk==""){
                    $("#msg2").html("<strong>이메일이 확인되지 않았습니다. 다시 시도해주시기 바랍니다.</strong>");
                }
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
        });
    });

    $("#id").on("input", () => {
        $("#idCheck").val("false");
        $("#msg").html("<strong style='color:red'>아이디가 바뀌었습니다. 다시 검증해주세요</strong>");
        $("#id").focus();
    })

    $("#email").on("input", () => {
        $("#emailCheck").val("false");
        $("#msg2").html("<strong style='color:red'>아이디가 바뀌었습니다. 다시 검증해주세요</strong>");
        $("#emailCheck").focus();
    })
});

function joinConfirm(f){
    if(f.password.value!=f.password2.value){
        alert("비밀번호와 비밀번호 확인이 서로 다릅니다.");
        f.password.focus();
        return false;
    }
    if(f.idCheck.value!="true"){
        alert("아이디 중복 체크를 하지 않으셨습니다.");
        return false;
    }
    if(f.emailCheck.value!="true"){
        alert("이메일 중복 체크를 하지 않으셨습니다.");
        return false;
    }
}