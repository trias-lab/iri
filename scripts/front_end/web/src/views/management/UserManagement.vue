<template>
    <div class="block">
        <div>
            <label>用户名：</label>
            <el-input v-model="select.username" class="input-small" placeholder="username"/>
            <label>账号：</label>
            <el-input v-model="select.address" class="input-small" placeholder="address"/>
            <label>邮箱：</label>
            <el-input type="text" class="input-small" v-model="select.email" auto-complete="off"
                      placeholder="email"></el-input>
            <label>性别</label>
            <el-select v-model="select.sex" placeholder="Choose sex">
                <el-option value="">All</el-option>
                <el-option value="1">Male</el-option>
                <el-option value="0">Female</el-option>
            </el-select>
            <el-button type="success" @click="searchUsers">Search</el-button>
            <hr/>
        </div>
        <el-table
                :data="tableData"
                style="width: 100%">
            <el-table-column
                    prop="username"
                    label="用户名">
            </el-table-column>
            <el-table-column
                    prop="address"
                    label="地址">
            </el-table-column>
            <el-table-column
                    prop="sex"
                    label="性别"
                    :formatter="formatSex">
            </el-table-column>
            <el-table-column
                    prop="email"
                    label="邮箱">
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建时间">
            </el-table-column>
            <el-table-column
                    prop="updateTime"
                    label="更新时间">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="100">
                <template slot-scope="scope">
                    <el-button @click="handleClick(scope.row)" type="primary" size="small">修改</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                @size-change="changeSize"
                @current-change="changeIndex"
                :current-page="currentPage"
                :page-sizes="[10, 20, 50]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
        </el-pagination>

        <el-dialog title="Edit" :visible.sync="editUserDialog" width="30%" style="text-align: left">
            <el-form :model="user" :rules="rules" ref="user">
                <el-form-item label="Username">
                    <el-input v-model="user.username" class="input-small" :readonly="true"/>
                </el-form-item>
                <el-form-item label="Address" prop="address">
                    <el-input v-model="user.address" class="input-small" placeholder="Address"/>
                </el-form-item>
                <el-form-item label="Email" prop="email">
                    <el-input v-model="user.email" class="input-small" placeholder="Email"/>
                </el-form-item>
                <el-form-item>
                    <el-radio-group v-model="user.sex">
                        <el-radio label="1">Male</el-radio>
                        <el-radio label="0">Female</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button style="float: right" @click="editUserDialog = false">Cancel</el-button>
                    <el-button type="primary" @click="updateUser" style="float: right;margin-right: 10px;">Submit
                    </el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
    import Cookies from "js-cookie";

    export default {
        data() {
            let checkEmail = (rule, value, callback) => {
                let reg = new RegExp(
                    "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"
                );
                if (!value) {
                    callback(new Error("邮箱地址不能为空"));
                } else if (!reg.test(value)) {
                    callback(new Error("请输入正确的邮箱地址"));
                } else {
                    callback();
                }
            };
            return {
                tableData: [],
                currentPage: 1,
                total: 0,
                pageSize: 10,
                select: {
                    username: "",
                    address: "",
                    sex: "",
                    email: ""
                },
                editUserDialog: false,
                user: {
                    username: "",
                    address: "",
                    email: ""
                },
                rules: {
                    email: [
                        {required: true, message: "Please type in email", trigger: "blur"},
                        {required: true, validator: checkEmail, message: "Email not correct", trigger: "blur"}
                    ]
                },
            }
        },
        created() {
            this.loadUserList();
        },
        methods: {
            changeSize(val) {
                this.pageSize = val;
                this.loadUserList();
            },
            changeIndex(val) {
                this.currentPage = val;
                this.loadUserList();
            },
            formatSex: function (row, column, cellValue) {
                if (cellValue === "1") {
                    return "男";
                } else if (cellValue === "0") {
                    return "女";
                }
            },
            loadUserList() {
                let request = {};
                let url = "/trias-resource/user/list";
                request.currentIndex = (this.currentPage - 1) * this.pageSize;
                request.pageSize = this.pageSize;
                request.username = this.select.username;
                request.address = this.select.address;
                request.sex = this.select.sex;
                request.email = this.select.email;
                this.axios.post(url, request, {headers: {'Authorization': 'Bearer ' + Cookies.get("UserToken")}}).then((res) => {
                    if (res.data["code"] === 1) {
                        let data = res.data["data"];
                        this.tableData = data.userList;
                        this.total = data.totalCount;
                    } else {
                        this.$alert(res.data["message"], this.messageOption.warning);
                    }
                }).catch((err) => {
                    console.error(err);
                    this.$alert("Query user error", this.messageOption.error);
                })
            },
            handleClick(row) {
                this.user = row;
                this.editUserDialog = true;
            },
            searchUsers() {
                this.currentPage = 1;
                this.loadUserList();
            },
            updateUser() {
                let url = "/trias-resource/user/updateUser";
                this.axios.post(url, this.user, {headers: {'Authorization': 'Bearer ' + Cookies.get("UserToken")}}).then((res) => {
                    if (res.data["code"] === 1) {
                        this.$alert("success",this.messageOption.success)
                        this.editUserDialog = false;
                        this.loadUserList();
                    } else {
                        this.$alert(res.data["message"], this.messageOption.warning);
                    }
                }).catch((err) => {
                    console.error(err);
                    this.$alert("Update user fail", this.messageOption.error);
                })
            }
        }
    }
</script>
<style>
    .input-small {
        width: 150px;
        margin-right: 5px;
    }

</style>
