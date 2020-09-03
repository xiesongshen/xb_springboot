var vm = new Vue({
    el:'#app',
    data:{
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        articleId:sessionStorage.getItem("articleId"),
        articleDetail:{},
        collectCount:"",
        collectUser:{}
    },
    methods:{
        toUpdate:function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        findArticleDetail:function () {
            axios({
                url:'/article/findById',
                params:{aid:this.articleId,uid:this.loginUser.id}
            }).then(response=>{
               /* console.log(response.data);*/
                if (response.data.flag){
                    this.articleDetail = response.data.data.article;
                    this.articleDetail.isCollect = response.data.data.isCollect;
                    this.collectCount = response.data.data.count;
                    this.collectUser = response.data.data.collectUser;
                } else {
                    layer.msg(response.data.message);
                }
            }).catch(error=>{
                layer.msg(error.message);
            });
        },
        changeCollect:function () {
            axios({
                url:'/favorite/changeCollect',
                params:{aid:this.articleId,uid:this.loginUser.id}
            }).then(response=>{
                layer.msg(response.data.message);
                this.findArticleDetail();

            }).catch(error=>{
                layer.msg(error.message);
            })
        },

        toDetail:function (user) {
            if (user.isSecret == 0) {
                layer.msg("对方设置了私密！");
                return;
            }
            axios({
                url: '/user/addLook',
                data: user,
                method: 'put'
            }).then(response => {
                if (response.data.flag) {
                    sessionStorage.setItem("userId", user.id);
                    location.href = '/html/user_detail.html';
                }
            }).catch(error => {
                layer.msg(error.message);
            })
        },

    },
    created:function () {
        this.findArticleDetail();
    }
});
/*
$(function () {
    var needRefresh = sessionStorage.getItem("need-refresh");
    if (needRefresh) {
        sessionStorage.removeItem("need-refresh");
        location.reload();
    }else {
        sessionStorage.setItem("need-refresh", true);
    }
});*/
