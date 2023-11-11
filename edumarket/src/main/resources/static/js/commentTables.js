$(document).ready( function () {
    $('#comment').DataTable({
        // sorting 화살표 제거
        "targets": 'no-sort',
        "bSort": false,
        "destroy": true,

        // 2번째 컬럼을 기준으로 내림차순 정렬
        order: [[2, 'desc']],
        pageLength : 5,
        searching: false, //검색 제거
        lengthChange: false, // show entries 제거
        info: false,

        language: {
            emptyTable: '작성된 댓글(이)가 없습니다.'
        }
    });
    $('#comment').css({
        'border':'none',
    });
} );