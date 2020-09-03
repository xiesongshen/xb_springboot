var vm = new Vue({
    el: '#app',
    data: {
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        title: '',
        status: '',
        meetingList: {},
        totalPage: 1,
        currPage: 1,
    },
    methods: {
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        search: function (page) {
            axios({
                url: `/meeting/search/${page}`,
                params: {title: this.title, status: this.status}
            }).then(response => {

                if (page < 1 || page > this.totalPage) {
                    layer.msg("已经到底啦！");
                    return;
                }
                if (response.data.flag) {

                    // 当前页数据
                    this.meetingList = response.data.data.pageResult.rows;

                    // 总页数
                    this.totalPage = response.data.data.pageResult.totalPage;

                    // 当前页
                    this.currPage = page;

                    // 搜索条件回显
                    this.title = response.data.data.title;
                    this.status = response.data.data.status;


                    if (this.meetingList.length == 0 && page > this.totalPage) {
                        this.search(1);
                    }

                } else {
                    layer.msg(response.data.message);
                }

            }).catch(error => {
                layer.msg(error.message);
            });
        },
        toDetail:function (mid) {
            sessionStorage.setItem("mid",mid);
            location.href="/html/meeting_detail.html";
        }
    },
    created: function () {
        this.search(1)
    }
});