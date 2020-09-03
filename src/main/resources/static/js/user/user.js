var vm = new Vue({
    el: '#app',
    data: {
        username: "",
        userList: {},
        totalPage: 1,
        currPage: 1,
        ids: [],
        loginUser: JSON.parse(localStorage.getItem("loginUser"))
    },
    methods: {
        search: function (page) {

            axios({
                url: `/user/search/${page}`,
                params: {username: this.username}
            }).then(response => {

                if (page < 1 || page > this.totalPage) {
                    layer.msg("已经到底啦！");
                    return;
                }
                if (response.data.flag) {

                    // 当前页数据
                    this.userList = response.data.data.pageResult.rows;

                    // 总页数
                    this.totalPage = response.data.data.pageResult.totalPage;

                    // 当前页
                    this.currPage = page;

                    // 搜索条件回显
                    this.username = response.data.data.username;

                    // 当前登录的用户关注过哪些用户
                    this.ids = response.data.data.userFocus;

                    // 回显关注过的用户
                    for (var i = 0; i < this.userList.length; i++) {
                        var user = this.userList[i];

                        if (this.ids.indexOf(user.id) == -1) {
                            user.focus = 0;
                        } else {
                            user.focus = 1;
                        }
                    }

                    if (this.userList.length == 0 && page > this.totalPage) {
                        this.search(1);
                    }

                } else {

                    layer.msg(response.data.message);
                }

            }).catch(error => {
                layer.msg(error.message);
            });

        },
        detail: function (user) {
            if (user.isSecret == 0) {
                layer.msg("对方设置了私密！");
                return;
            }

            if (user.id != this.loginUser.id) {
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
            } else {
                sessionStorage.setItem("userId", user.id);
                location.href = '/html/user_detail.html';
            }

        },
        focus: function (uid, event) {
            if (uid == this.loginUser.id) {
                layer.msg("不能关注自己喔")

                event.path[0].checked = false;
                return;
            }
            axios({
                url: `/userFocus/focus/${uid}`,
            }).then(response => {
                layer.msg(response.data.message);
                this.search(this.currPage);
            }).catch(error => {
                layer.msg(error.message);
            })
        },
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
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
