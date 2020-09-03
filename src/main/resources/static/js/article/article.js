var vm = new Vue({
    el: '#app',
    data: {
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        title: "",
        articleList: {},
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
                url: `/article/search/${page}`,
                params: {title: this.title}
            }).then(response => {

                if (page < 1 || page > this.totalPage) {
                    layer.msg("已经到底啦！");
                    return;
                }
                if (response.data.flag) {

                    // 当前页数据
                    this.articleList = response.data.data.pageResult.rows;

                    // 总页数
                    this.totalPage = response.data.data.pageResult.totalPage;

                    // 当前页
                    this.currPage = page;

                    // 搜索条件回显
                    this.title = response.data.data.title;


                    if (this.articleList.length == 0 && page > this.totalPage) {
                        this.search(1);
                    }

                } else {
                    layer.msg(response.data.message);
                }

            }).catch(error => {
                layer.msg(error.message);
            });

        },
        toDetail: function (article) {
            if (article.userId != this.loginUser.id) {
                axios({
                    url: '/article/addBrowseCount',
                    data: article,
                    method: 'post'
                }).then(response => {
                    if (response.data.flag) {
                        article = response.data.data;
                        console.log(article);
                        sessionStorage.setItem("articleId", article.id);
                        location.href = "/html/article_detail.html";
                    }
                }).catch(error => {
                    layer.msg(error.message);
                })
            } else {
                sessionStorage.setItem("articleId", article.id);
                location.href = "/html/article_detail.html";
            }

        }
    },
    created: function () {
        this.search(1);
    }
});
/*$(function () {
    var needRefresh = sessionStorage.getItem("need-refresh");
    if (needRefresh) {
        sessionStorage.removeItem("need-refresh");
        location.reload();
    }
});*/
