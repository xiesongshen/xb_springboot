var vm = new Vue({
    el:'#app',
    data:{
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        article:{}
    },
    methods:{
        toUpdate:function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        doInsert:function () {
            if (this.article.title == undefined || this.article.content == undefined){
                layer.msg("请输入有效信息");
            }else {
                axios({
                    url:'/article/doInsert',
                    data: this.article,
                    method:'put'
                }).then(response=>{
                    if (response.data.flag){
                        layer.msg(response.data.message);

                        location.href="/html/article.html";

                    }else {
                        layer.msg(response.data.message);
                    }
                }).catch(error=>{
                    layer.msg(error.message);
                })
            }
        },
        checkTitle:function () {
            if (this.article.title == undefined || this.article.title.length == 0){
                layer.msg("标题不能为空")
            }
        },
        checkContent:function () {
            if (this.article.content == undefined || this.article.content.length == 0){

                layer.msg("内容不能为空")
            }
        }
    },
    created:function () {

    }
});