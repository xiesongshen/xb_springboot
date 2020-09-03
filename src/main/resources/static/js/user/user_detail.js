var vm = new Vue({
    el: '#app',
    data: {
        uid: sessionStorage.getItem("userId"),
        user: {},
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        focusCount: ''
    },
    methods: {
        userDetail: function () {
            axios({
                url: '/user/findById',
                params: {uid: this.uid}
            }).then(response => {
                if (response.data.flag) {
                    this.user = response.data.data;
                }
            }).catch(error => {
                layer.msg(error.message);
            })

            axios({
                url: '/userFocus/selectFocusCount',
                params: {userId: this.uid}
            }).then(response => {
                if (response.data.data == null) {
                    this.focusCount = 0;
                } else {
                    this.focusCount = response.data.data;
                }
            }).catch(error => {
                layer.msg(error.message);
            })
        },
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
    },
    created: function () {
        this.userDetail();
    }
});
/*
$(function () {
    sessionStorage.setItem("need-refresh", true);
});*/
