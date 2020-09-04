var vm = new Vue({
    el: '#app',
    data: {
        loginUser: JSON.parse(localStorage.getItem("loginUser")),
        meeting:{},
        deptList:{},
        deptUser:{},
        joinUids:[]
    },
    methods: {
        toUpdate: function () {
            sessionStorage.setItem("userId", this.loginUser.id);
            location.href = '/html/user_look.html';
        },
        save:function () {
            if (this.meeting.startTime == undefined || this.meeting.endTime == undefined
                || this.meeting.title == undefined || this.meeting.content == undefined
                || this.meeting.deptId == undefined || this.joinUids == undefined) {
                layer.msg("請輸入會議詳情");

                return;
            }

            this.meeting.makeUser = this.joinUids.toString();
            this.meeting.startTime = this.meeting.startTime.replace("T"," ");
            this.meeting.endTime = this.meeting.endTime.replace("T" ," ");

            axios({
                url:'/meeting/save',
                data: this.meeting,
                method:'put'
            }).then(response=>{
                if (response.data.flag){
                    location.href="/html/meeting.html";
                }
            }).catch(error=>{
                layer.msg(error.message);
            })

        },

        selectDeptUser:function (deptId) {
            axios({
                url:'/user/findByDeptId',
                params:{deptId:deptId}
            }).then(response=>{
                this.deptUser =  response.data.data;
            }).catch(error=>{
                layer.msg(error.message);
            }).finally(res=>{
                $(".selectpicker").selectpicker('refresh');
            })

        },
        selectAllDept:function () {
            axios({
                url:'/dept/deptList',
            }).then(response=>{
                this.deptList = response.data.data;
            }).catch(error=>{
                layer.msg(error.message);
            })
        },
        checkTime:function () {
            if (this.meeting.endTime<this.meeting.startTime){
                layer.msg("开始时间不允许大于结束时间噢");
                this.meeting.endTime = "";
            }
        }
    },
    created: function () {
        this.selectAllDept();
    }
});