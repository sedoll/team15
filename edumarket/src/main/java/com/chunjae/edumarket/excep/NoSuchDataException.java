package com.chunjae.edumarket.excep;

public class NoSuchDataException extends RuntimeException {
    public NoSuchDataException(String message){
        super(message);
    }
    /*
    getUserList : 만약, 테이블이 비어 있다면, 빈 리스트 반환
    getUser : 일치하는 데이터가 없으면, null 로 반환
    updateUser : update 된 데이터가 없으면, 0 반환
    removeUser : delete 된 데이터가 없으면, 0 반환
    joinPro : insert된 데이터가 없으면, 0 반환
    */
}
