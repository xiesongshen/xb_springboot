var vm = new Vue({
    el: '#app',
    data: {
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        deptData: {}
    },
    methods: {
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        findDeptAll: function () {
            axios.get("/dept/findDeptAll").then((response) => {
                if (response.data.flag) {
                    this.deptData = response.data.data;
                } else {
                    layer.msg(response.data.msg)
                }
            })
        },
        showUser: function (event) {
            if (event.target.nextSibling.nextSibling.classList == 'collapse deptDetail') {
                event.target.nextSibling.nextSibling.classList.add('show')
            } else {
                event.target.nextSibling.nextSibling.classList.remove('show')
            }
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

    },
    created: function () {
        this.findDeptAll();
    }
});
$(function () {
    var needRefresh = sessionStorage.getItem("need-refresh");
    if (needRefresh) {
        sessionStorage.removeItem("need-refresh");
        location.reload();
    }
});