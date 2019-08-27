<template>
    <div>
        <el-row class="container">
            <el-col :span="24" class="header">
                <el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
                    {{collapsed?'':sysName}}
                </el-col>
                <el-col :span="4" class="userinfo">
                    <el-dropdown trigger="hover">
                        <span class="el-dropdown-link userinfo-inner">{{$store.state.userInfo.username}}</span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item @click.native="settings">Settings</el-dropdown-item>
                            <el-dropdown-item @click.native="logout">Logout</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </el-col>
            </el-col>
            <el-col :span="24" class="main">
                <aside :class="collapsed?'menu-collapsed':'menu-expanded'">
                    <!--导航菜单-->
                    <el-menu :default-active="$route.path" class="el-menu-vertical-demo" @open="handleopen"
                             @close="handleclose" @select="handleselect"
                             unique-opened router v-show="!collapsed">
                        <template v-for="(item,index) in $router.options.routes"
                                  v-if="!item.hidden && $store.state.rootMap[item.rootName] === 1">
                            <el-submenu :index="index+''">
                                <template slot="title"><i :class="item.iconCls"></i>{{item.name}}</template>
                                <el-menu-item v-for="child in item.children" :index="child.path" :key="child.path"
                                              v-if="!child.hidden && $store.state.pathMap[child.path] === 1">
                                    {{child.name}}
                                </el-menu-item>
                            </el-submenu>
                        </template>
                    </el-menu>
                </aside>
                <section class="content-container">
                    <div class="grid-content bg-purple-light">
                        <el-col :span="24" class="breadcrumb-container">
                            <strong class="title">{{$route.name}}</strong>
                            <el-breadcrumb separator="/" class="breadcrumb-inner">
                                <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
                                    {{ item.name }}
                                </el-breadcrumb-item>
                            </el-breadcrumb>
                        </el-col>
                        <el-col :span="24" class="content-wrapper">
                            <transition name="fade" mode="out-in">
                                <router-view></router-view>
                            </transition>
                        </el-col>
                    </div>
                </section>
            </el-col>
        </el-row>

        <el-dialog title="Edit" :visible.sync="editUserDialog" width="30%" style="text-align: left">
            <el-form :model="user" :rules="rules" ref="user">
                <el-form-item label="Username">
                    <el-input v-model="user.username" class="input-small" :readonly="true"/>
                </el-form-item>
                <el-form-item label="Address">
                    <el-input v-model="user.address" class="input-small" :readonly="true"/>
                    <el-button class="m110" type="text" size="medium" v-clipboard:copy="user.address" v-clipboard:success="onCopy" v-clipboard:error="onError">Copy</el-button>
                </el-form-item>
                <el-form-item label="Sign">
                    <el-input v-model="user.sign" class="input-large" :readonly="true"/>
                    <el-button class="m110" type="text" size="medium" v-clipboard:copy="user.sign" v-clipboard:success="onCopy" v-clipboard:error="onError">Copy</el-button>
                </el-form-item>
                <el-form-item label="PrivateKey">
                    <el-input v-model="user.privateKey" class="input-large" :readonly="true"/>
                    <el-button class="m110" type="text" size="medium" v-clipboard:copy="user.sign" v-clipboard:success="onCopy" v-clipboard:error="onError">Copy</el-button>
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
    import Cookies from "js-cookie"

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
                sysName: 'TRIAS',
                collapsed: false,
                sysUserName: '',
                sysUserAvatar: '',
                form: {
                    name: '',
                    region: '',
                    date1: '',
                    date2: '',
                    delivery: false,
                    type: [],
                    resource: '',
                    desc: ''
                },
                editUserDialog: false,
                user: this.$store.state.userInfo,
                rules: {
                    email: [
                        {required: true, message: "Please type in email", trigger: "blur"},
                        {required: true, validator: checkEmail, message: "Email not correct", trigger: "blur"}
                    ]
                }
            }
        },
        methods: {
            onSubmit() {
                console.log('submit!');
            },
            logout() {
                Cookies.remove("UserToken");
                this.$router.push("/login")
            },
            settings() {
                this.editUserDialog = true;
            },
            onCopy(e){
                this.$alert("success!",this.messageOption.success);
            },
            onError(e){
                console.log(e);
            },
            handleopen() {
                //console.log('handleopen');
            },
            handleclose() {
                //console.log('handleclose');
            },
            handleselect: function (a, b) {
            },
            updateUser() {
                let url = "/trias-resource/user/updateUser";
                this.axios.post(url, this.user, {headers: {'Authorization': 'Bearer ' + Cookies.get("UserToken")}}).then((res) => {
                    if (res.data["code"] === 1) {
                        this.$alert("success", this.messageOption.success)
                        this.editUserDialog = false;
                    } else {
                        this.$alert(res.data["message"], this.messageOption.warning);
                    }
                }).catch((err) => {
                    console.error(err);
                    this.$alert("Update user fail", this.messageOption.error);
                })
            },

            //折叠导航栏
            collapse: function () {
                this.collapsed = !this.collapsed;
            },
            showMenu(i, status) {
                this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-' + i)[0].style.display = status ? 'block' : 'none';
            }
        }
    }

</script>

<style scoped lang="scss">
    @import '~scss_vars';

    .container {
        position: absolute;
        top: 0px;
        bottom: 0px;
        width: 100%;

        .header {
            height: 60px;
            line-height: 60px;
            background: $color-primary;
            color: #fff;

            .userinfo {
                text-align: right;
                padding-right: 35px;
                float: right;

                .userinfo-inner {
                    cursor: pointer;
                    color: #fff;

                    img {
                        width: 40px;
                        height: 40px;
                        border-radius: 20px;
                        margin: 10px 0px 10px 10px;
                        float: right;
                    }
                }
            }

            .logo {
                //width:230px;
                height: 60px;
                font-size: 22px;
                padding-left: 20px;
                padding-right: 20px;
                border-color: rgba(238, 241, 146, 0.3);
                border-right-width: 1px;
                border-right-style: solid;

                img {
                    width: 40px;
                    float: left;
                    margin: 10px 10px 10px 18px;
                }

                .txt {
                    color: #fff;
                }
            }

            .logo-width {
                width: 230px;
            }

            .logo-collapse-width {
                width: 60px
            }

            .tools {
                padding: 0px 23px;
                width: 14px;
                height: 60px;
                line-height: 60px;
                cursor: pointer;
            }
        }

        .main {
            display: flex;
            // background: #324057;
            position: absolute;
            top: 60px;
            bottom: 0px;
            overflow: hidden;

            aside {
                flex: 0 0 230px;
                width: 230px;
                // position: absolute;
                // top: 0px;
                // bottom: 0px;
                .el-menu {
                    height: 100%;
                }

                .collapsed {
                    width: 60px;

                    .item {
                        position: relative;
                    }

                    .submenu {
                        position: absolute;
                        top: 0px;
                        left: 60px;
                        z-index: 99999;
                        height: auto;
                        display: none;
                    }

                }
            }

            .menu-collapsed {
                flex: 0 0 60px;
                width: 60px;
            }

            .menu-expanded {
                flex: 0 0 230px;
                width: 230px;
            }

            .content-container {
                // background: #f1f2f7;
                flex: 1;
                // position: absolute;
                // right: 0px;
                // top: 0px;
                // bottom: 0px;
                // left: 230px;
                overflow-y: scroll;
                padding: 20px;

                .breadcrumb-container {
                    //margin-bottom: 15px;
                    .title {
                        width: 200px;
                        float: left;
                        color: #475669;
                    }

                    .breadcrumb-inner {
                        float: right;
                    }
                }

                .content-wrapper {
                    background-color: #fff;
                    box-sizing: border-box;
                }
            }
        }
    }

    .input-small {
        width: 250px;
        margin-right: 5px;
    }

    .input-large {
        width: 350px;
        margin-right: 5px;
    }
</style>
