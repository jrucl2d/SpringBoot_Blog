const index = {
    init: function() {
        const saveBtn = document.querySelector("#btn-save");
        if(saveBtn){
            saveBtn.addEventListener("click", () => {
                this.save();
            })
        }
        const deleteBtn = document.querySelector("#btn-delete");
        if(deleteBtn){
            deleteBtn.addEventListener("click", () => {
                this.deleteByID();
            })
        }
        const updateBtn = document.querySelector("#btn-update");
        if(updateBtn){
            updateBtn.addEventListener("click", () => {
                this.update();
            })
        }
        const replyBtn = document.querySelector("#btn-reply-save");
        if(replyBtn){
            replyBtn.addEventListener("click", () => {
                this.saveReply();
            })
        }
    },
    save : async function() {
        const data = {
            title : document.querySelector("#title").value,
            content : document.querySelector("#content").value,
        }
        try{
            await axios.post("/api/board", data);
            alert("글쓰기가 완료되었습니다.");
            location.href="/";
        } catch(err){
            alert(err);
        }
    },
    deleteByID : async function() {
        const id = document.querySelector("#id").textContent;
        try{
            await axios.delete("/api/board/" + id);
            alert("삭제가 완료되었습니다.");
            location.href="/";
        } catch(err){
            alert(err);
        }
    },
    update : async function() {
        const id = document.querySelector("#updateID").value;
        const data = {
            title : document.querySelector("#title").value,
            content : document.querySelector("#content").value,
        }
        try{
            await axios.put("/api/board/" + id, data);
            alert("수정이 완료되었습니다.");
            location.href="/";
        } catch(err){
            alert(err);
        }
    },
    saveReply : async function() {
        const data = {
            userId : document.querySelector("#userId").value,
            boardId : document.querySelector("#boardId").value,
            content : document.querySelector("#reply-content").value,
        }
        try{
            await axios.post(`/api/board/${data.boardId}/reply`, data);
            alert("댓급 입력이 완료되었습니다.");
            location.href=`/board/${data.boardId}`;
        } catch(err){
            alert(err);
        }
    },
    replyDelete : async function(boardId, replyId) {
        try{
            await axios.delete(`/api/board/${boardId}/reply/${replyId}`);
            alert("댓글 삭제 완료");
            location.href = `/board/${boardId}`;
        } catch(err){
            alert(err);
        }
    }
}
index.init();