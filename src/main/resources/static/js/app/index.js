var main = {
    init : function () {
        var _this = this;

        // 1. 등록 버튼 이벤트
        var saveBtn = document.getElementById('btn-save');
        if(saveBtn){
            saveBtn.addEventListener('click', function () {
                _this.save();
            });
        }

        // 2. 수정 버튼 이벤트 (추가됨)
        var updateBtn = document.getElementById('btn-update');
        if(updateBtn){
            updateBtn.addEventListener('click', function () {
                _this.update();
            });
        }

        // 3. 삭제 버튼 이벤트 (추가됨)
        var deleteBtn = document.getElementById('btn-delete');
        if(deleteBtn){
            deleteBtn.addEventListener('click', function () {
                _this.delete();
            });
        }
    },
    save : function () {
        // ... 기존 save 코드 그대로 유지 ...
        var data = {
            title: document.getElementById('title').value,
            author: document.getElementById('author').value,
            content: document.getElementById('content').value
        };

        fetch('/api/v1/posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify(data)
        }).then(function (response) {
            if (response.ok) {
                alert('글이 등록되었습니다.');
                window.location.href = '/';
            } else {
                alert('등록에 실패했습니다.');
            }
        }).catch(function (error) {
            alert(JSON.stringify(error));
        });
    },
    // ▼ 수정 로직 (PUT 요청)
    update : function () {
        var data = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        var id = document.getElementById('id').value;

        fetch('/api/v1/posts/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify(data)
        }).then(function (response) {
            if (response.ok) {
                alert('글이 수정되었습니다.');
                window.location.href = '/';
            } else {
                alert('수정에 실패했습니다.');
            }
        }).catch(function (error) {
            alert(JSON.stringify(error));
        });
    },
    // ▼ 삭제 로직 (DELETE 요청)
    delete : function () {
        var id = document.getElementById('id').value;

        fetch('/api/v1/posts/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        }).then(function (response) {
            if (response.ok) {
                alert('글이 삭제되었습니다.');
                window.location.href = '/';
            } else {
                alert('삭제에 실패했습니다.');
            }
        }).catch(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();
