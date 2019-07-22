<template>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-position="left" label-width="0px"
             class="demo-ruleForm login-container">
        <span style="cursor: pointer;color: #1d90e6" @click="loginSystem">Login</span>
        <h3 class="title">Sign Up</h3>
        <el-form-item prop="username">
            <el-input type="text" v-model="ruleForm.username" auto-complete="off" placeholder="username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
            <el-input type="password" v-model="ruleForm.password" auto-complete="off"
                      placeholder="password"></el-input>
        </el-form-item>
        <el-form-item style="width:100%;">
            <el-button type="success" style="width: 100%;" @click.native.prevent="userRegister">Sign Up</el-button>
        </el-form-item>
    </el-form>
</template>

<script>

    export default {
        data() {
            return {
                ruleForm: {
                    username: '',
                    password: '',
                },
                rules: {
                    username: [
                        {required: true, message: "Please type in username", trigger: "blur"},
                    ],
                    password: [
                        {required: true, message: "Please type in password", trigger: "blur"},
                    ],
                },
            };
        },
        methods: {
            userRegister() {
                this.$refs.ruleForm.validate((valid) => {
                    if (valid) {
                        // let request = {username: this.ruleForm2.username, password: this.ruleForm2.password};
                        let url = "/trias-resource/user/register";
                        this.axios.post(url, this.ruleForm).then((res) => {
                            if (res.data["code"] === 1) {
                                this.$alert("success", this.messageOption.success);
                                this.$router.push("/login");
                            } else {
                                console.log(res.data["message"]);
                                this.$alert(res.data["message"], this.messageOption.warning);
                            }
                        }).catch((err) => {
                            console.error(err);
                            this.$alert("login happens a error", this.messageOption.error);
                        })
                    } else {
                        this.$alert("Please check the input", this.messageOption.error);
                        return false;
                    }
                });
            },
            loginSystem() {
                this.$router.push("/login")
            }
        }
    }

</script>

<style lang="scss" scoped>
    .login-container {
        /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
        -webkit-border-radius: 5px;
        border-radius: 5px;
        -moz-border-radius: 5px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;

        .title {
            margin: 0px auto 40px auto;
            text-align: center;
            color: #505458;
        }

        .remember {
            margin: 0px 0px 35px 0px;
        }
    }
</style>
