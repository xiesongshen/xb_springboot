var vm = new Vue({
    el: '#app',
    data: {
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        mid: sessionStorage.getItem("mid"),
        meetingDetail: {},
        shouldCount: '',
        realCount: '',
        shouldJoin:[]
    },
    methods: {
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        findById: function () {
            axios({
                url: '/meeting/findById',
                params: {mid: this.mid}
            }).then(response => {
                if (response.data.flag) {

                    //会议信息
                    this.meetingDetail = response.data.data.meeting;

                    //应该参加会议的人数
                    this.shouldCount = response.data.data.shouldCount;

                    //已参加人数
                    this.realCount = response.data.data.realCount;

                    //是否已参加会议
                    this.meetingDetail.isJoin = response.data.data.isJoin;

                    this.meetingDetail.startNoTime = response.data.data.meeting.startTime.split(" ")[0];

                    //应该参加会议的人员
                    this.shouldJoin = response.data.data.shouldJoin;

                } else {
                    layer.msg(response.data.message);
                }
            })
        },
        changeJoin: function () {
            if (this.shouldJoin.indexOf(this.loginUser.id+"") == -1){
                layer.msg("该会议不需要您参加噢");
                return;
            }

            if (this.meetingDetail.status==2){
                layer.msg("会议已结束");
                return;
            }

            axios({
                url: '/meeting/changeJoin',
                params: {mid: this.mid, uid: this.loginUser.id}
            }).then(response => {
                layer.msg(response.data.message);
                this.findById();
            }).catch(error => {
                layer.msg(error.message);
            })
        }
    },
    created: function () {
        this.findById();
    }
});