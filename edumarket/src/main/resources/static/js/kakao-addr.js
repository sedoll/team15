function findAddr(){
    new daum.Postcode({
        oncomplete:function(data){
            console.log(data);
            var roadAddr = data.roadAddress;
            var jibunAddr = data.jibunAddress;
            document.getElementById("postcode").value = data.zonecode;
            if(roadAddr !== ''){
                document.getElementById("addr1").value = roadAddr;
            } else if(jibunAddr !== ''){
                document.getElementById("addr1").value = jibunAddr;
            }
            document.getElementById("addr2").focus();
        }
    }).open();
}