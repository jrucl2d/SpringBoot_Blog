const index = {
    init: function() {
        const saveBtn = document.querySelector("#btn-save");
        if(saveBtn){
            saveBtn.addEventListener("click", () => {
                this.save();
            })
        }
        const updateBtn = document.querySelector("#btn-update");
        if(updateBtn){
            updateBtn.addEventListener("click", () => {
                this.update();
            })
        }
        // const loginBtn = document.querySelector("#btn-login");
        // if(loginBtn){
        //     loginBtn.addEventListener("click", () => {
        //         this.login();
        //     })
        // }
    },
    save : async function() {
        const data = {
            username : document.querySelector("#username").value,
            password : document.querySelector("#password").value,
            email : document.querySelector("#email").value,
        }
        try{
            const result = await axios.post("/auth/joinProc", data);
            if(result.data.status  === 500){
                alert("회원가입에 실패했습니다.");
                return;
            }
            alert("회원가입이 완료되었습니다.");
            location.href="/";
        } catch(err){
            alert(err);
        }
    },
    update : async function() {
        const data = {
            id : document.querySelector("#updateID").value,
            username : document.querySelector("#username").value,
            password : document.querySelector("#updatePassword").value,
            email : document.querySelector("#updateEmail").value,
        }
        try{
            await axios.put("/user", data);
            alert("회원정보 수정이 완료되었습니다.");
            location.href="/";
        } catch(err){
            alert(err);
        }
    },
    // login : async function() {
    //     const data = {
    //         username : document.querySelector("#username").value,
    //         password : document.querySelector("#password").value,
    //     }
    //     try{
    //         await axios.post("/api/user/login", data);
    //         alert("로그인이 완료되었습니다.");
    //         location.href="/";
    //     } catch(err){
    //         alert(err);
    //     }
    // }
}
index.init();