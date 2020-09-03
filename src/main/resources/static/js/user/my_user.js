var vm = new Vue({
    el: '#app',
    data: {
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        myUser: {},
        totalPage: 1,
        currPage: 1,
    },
    methods: {
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        search: function (page) {

            if (page < 1 || page > this.totalPage) {
                layer.msg("已经到底啦！");
                return;
            }

            axios({
                url: `/user/myUserList/${page}`,
                params: {uid: this.loginUser.id}
            }).then(response => {
                if (response.data.flag) {
                    // 当前页数据
                    this.myUser = response.data.data.rows;

                    // 总页数
                    this.totalPage = response.data.data.totalPage;

                    // 当前页
                    this.currPage = page;
                } else {
                    layer.msg(response.data.message);
                }
            }).catch(error => {
                layer.msg(error.message);
            })
        },
        toDetail: function (user) {
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
        delFocus: function (focusId) {
            axios({
                url: '/userFocus/delFocus',
                params: {userId: this.loginUser.id, focusId: focusId}
            }).then(response => {
                if (response.data.flag) {

                    layer.msg(response.data.message);

                    if (this.myUser.length == 1) {
                        if (this.currPage == 1) {
                            this.search(1);
                        } else {
                            this.search(this.currPage - 1);
                        }
                    } else {
                        this.search(this.currPage);
                    }

                } else {
                    layer.msg(response.data.message);
                }
            }).catch(error => {
                layer.msg(error.message);
            })
        }

    },
    created: function () {
        this.search(1);
    }
});
/*
$(function () {
    var needRefresh = sessionStorage.getItem("need-refresh");
    if (needRefresh) {
        sessionStorage.removeItem("need-refresh");
        location.reload();
    }
});*/
