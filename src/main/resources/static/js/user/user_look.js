var vm = new Vue({
    el: '#app',
    data: {
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        deptList: {},
        fCount: ''
    },
    methods: {
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        selectDeptAndFocusCount: function () {
            axios({
                url: '/dept/deptList',
            }).then(response => {
                this.deptList = response.data.data
            }).catch(error => {
                layer.msg(error.message);
            });

            axios({
                url: '/userFocus/selectFCount',
                params: {userId: this.loginUser.id}
            }).then(response => {
                if (response.data.data == null) {
                    this.fCount = 0;
                } else {
                    this.fCount = response.data.data;
                }
            }).catch(error => {
                layer.msg(error.message);
            })
        },
        doUpdate: function () {
            axios({
                url: '/user/doUpdate',
                data: this.loginUser,
                method: 'put'
            }).then(response => {
                if (response.data.flag) {
                    layer.msg(response.data.message);
                    // 更新localStorage中的loginUser
                    localStorage.setItem("loginUser", JSON.stringify(response.data.data));
                }
            }).catch(error => {
                layer.msg(error.message);
            })
        },
        uploadPic: function (event) {
            // 模拟一个表单
            let formData = new FormData();

            // 往表单追加文件
            formData.append("pic", document.getElementById("picFile").files[0]);

            axios.post(`/user/uploadPic`, formData, {
                contentType: 'multipart/form-data'
            }).then(response => {

                if (response.data.flag) {
                    this.loginUser.pic = response.data.data;

                    // 更新localStorage中的图片地址
                    localStorage.setItem("loginUser", JSON.stringify(this.loginUser));
                }
                layer.msg(response.data.message)
            })
            // // 无论上传成功还是失败都把文件输入框置空
            event.target.value = '';
        }
    },
    created: function () {
        this.selectDeptAndFocusCount();
    }
});
/*
$(function () {
    sessionStorage.setItem("need-refresh", true);
})*/
